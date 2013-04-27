//Pacote da aplicacao
package gameEngine;

public class CIntervaloTempo 
{
	//Atributos
	public int iTempoFinal;
	public int iTempoAtual; 
	public CGerenteTempo vrGerenciadorTempo=null;
	
	//Construtor da classe
	public CIntervaloTempo()
	{
		iTempoAtual = 0;
		iTempoFinal = 1000;
		vrGerenciadorTempo = CGerenteTempo.retornaGerenteTempo();
	}
	
	//Construtor da classe com parametro
	public CIntervaloTempo(int pInterval)
	{
		iTempoAtual = 0;
		iTempoFinal = pInterval;
		vrGerenciadorTempo = CGerenteTempo.retornaGerenteTempo();
	}
	
	//Atualiza o intervalo de tempo
	public void atualiza()
	{
		if (vrGerenciadorTempo != null)
		{
			iTempoAtual += (int)vrGerenciadorTempo.retornaTempoQuadroAtual();
		}
	}
	
	//Verifica se o intervalo foi finalizado
	public boolean tempoFinalizado()
	{
		if (iTempoAtual >= iTempoFinal)
			return true;

		return false;
	}
	
	//Reinicia o intervalo de tempo
	public void reiniciaTempo()
	{
		iTempoAtual %= iTempoFinal;
	}
	
	//Reinicia o intervalod de tempo com um novo valor
	public void reiniciaTempo(int novoIntervalo)
	{
		iTempoAtual = 0;
		iTempoFinal = novoIntervalo;
	}
	
	//Retorna o tempo atual em MS
	public int retornaTempoMS()
	{
		return iTempoAtual;
	}
	

	//Retorna tempo em S
	public float retornaTempoS()
	{
		return (float)iTempoAtual/1000;
	}

	
	//Retorna o tempo do quadro atual
	public int retornaTempoQuadroAtual()
	{	
		return (int)vrGerenciadorTempo.retornaTempoQuadroAtual();
	}
	
	//Libera recursos
	public void liberaRecursos()
	{
		vrGerenciadorTempo = null;
	}
}
