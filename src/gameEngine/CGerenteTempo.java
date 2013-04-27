//Pacote da aplicacao
package gameEngine;

public class CGerenteTempo 
{
	//Atributos da classe
	private long 	lTempoUltimoQuadro;
	private long 	lTempoQuadroAtual;
	private long 	lTempoAcumulado;
	private long	lContadorQuadros;
	private long	lFPS;
	private final int MININTERVAL = 5;
	
	//Variavel estatica para implementar o Singleton
	private static CGerenteTempo vrGerenteTempo = null;
	
	//Construtor da classe
	private CGerenteTempo()
	{ 
		lTempoUltimoQuadro = System.currentTimeMillis();
		lTempoQuadroAtual = 0;
		lTempoAcumulado = 0;
		lContadorQuadros = 0;
		lFPS = 0;
	}
	
	//Metodo utilizado para inicializar o gerente de tempo
	public static void inicializaGerente()
	{
		if (vrGerenteTempo == null)
		{
			vrGerenteTempo = new CGerenteTempo();
		}
	}
	
	//Metodo utilizado para atualizar o gerente do tempo
	public static void atualiza()
	{
		vrGerenteTempo.atualizaTempo();
	}
	
	//Metodo estatico criado para o padrao singleton
	public static CGerenteTempo retornaGerenteTempo()
	{
		inicializaGerente();
		return vrGerenteTempo;
	}
	
	//Metodo que retorna o tempo do ultimo quadro
	public long retornaTempoUltimoQuadro()
	{
		return lTempoUltimoQuadro;
	}
	
	//Retorna o tempo do quadro atual
	long retornaTempoQuadroAtual()
	{
		return lTempoQuadroAtual;
	}
	
	//Retorna o tempo total acumulado
	public long retornaTempoAcumulado()
	{
		return lTempoAcumulado;
	}
	
	//Retorna o valor do contador de quadros
	public long retornaContadorQuadros()
	{
		return lContadorQuadros;
	}
	
	//Retorna o total de quadros contados por segundo
	public long retornaFPS()
	{
		return lFPS;
	}
	
	//Metodo utilizado para liberar recursos
	public void liberaRecursos()
	{
		
	}
	
	//Metodo utilizado para reiniciar o tempo
	public void reinicia()
	{
		lTempoUltimoQuadro = System.currentTimeMillis();
	}
	
	//Atualiza os dados do tempo
	private void atualizaTempo()
	{
		lTempoQuadroAtual = 0;
		long lTempoAtual=0;

		do
		{
			lTempoAtual = System.currentTimeMillis();
			lTempoQuadroAtual = (lTempoAtual > lTempoUltimoQuadro) ? (lTempoAtual - lTempoUltimoQuadro) : 0;
			lTempoUltimoQuadro = (lTempoAtual >= lTempoUltimoQuadro) ? lTempoUltimoQuadro : lTempoAtual;
		}
		while(! (lTempoQuadroAtual >= MININTERVAL));
		
		lTempoAcumulado += lTempoQuadroAtual;
		lContadorQuadros++;

		if (lTempoAcumulado >= 1000)
		{
			lFPS = lContadorQuadros / 1000;
			lContadorQuadros = 0;
			lTempoAcumulado   = 0;
		}

		lTempoUltimoQuadro = lTempoAtual;
	}
}
