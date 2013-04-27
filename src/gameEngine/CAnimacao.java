//Classe desenvolvida para o controle de uma animacao

//Pacote da aplicacao
package gameEngine;

public class CAnimacao 
{
	//Atributos da classe
	public boolean bRepeticao;
	public int iQuadroAtual;
	private CQuadro[] vetorQuadros=null;
	private int iFPS;
	private CIntervaloTempo vrTempo = null; 
	
	//Construtor da classe
	public CAnimacao(CQuadro[] pQuads)
	{
		iFPS = 1;
		iQuadroAtual=0;
		vetorQuadros = pQuads;
		bRepeticao = true;
	}
	
	//Retorna o total de quadros da animacao
	public int getTotalQuadros()
	{
		return vetorQuadros.length;
	}
	
	//Verifica se a animacao ja acabou ou nao
	public boolean animacaoFinalizada()
	{
		if (bRepeticao)
		{
			return false;
		}
		
		return (iQuadroAtual >= vetorQuadros.length);
	}
	
	//Devolve indice do quadro atual
	public int retornaIndiceQuadroAtual()
	{
		return retornaQuadroAtual().iIndiceQuadro;
	}
	
	//Devolve o objeto do quadro atual
	public CQuadro retornaQuadroAtual()
	{
		if (iQuadroAtual >= 0 && iQuadroAtual < vetorQuadros.length)
		{
			return vetorQuadros[iQuadroAtual];
		}
		
		return vetorQuadros[vetorQuadros.length-1];
	}
	
	//Reinicia o tempo e indice da animacao
	public void reiniciaAnimacao()
	{
		vrTempo.reiniciaTempo((iFPS > 0) ? 1000 / iFPS : 1);
		iQuadroAtual = 0;
	}
	
	//Configura o intervalo da animacao
	public void configuraIntervalo(int interval)
	{
		if (vrTempo == null)
		{
			vrTempo = new CIntervaloTempo();
		}
		
		iFPS = interval;
		vrTempo.reiniciaTempo((iFPS>0) ? 1000/iFPS : 1);
	}
	
	//Atualiza o quadro da animacao conforme o tempo decorrido
	public void atualizaAnimacao()
	{
		vrTempo.atualiza();
		
		if (vrTempo.tempoFinalizado())
		{
			if(bRepeticao)
			{
				iQuadroAtual++;
				iQuadroAtual %= vetorQuadros.length;
			}
			else
			{
				iQuadroAtual += (iQuadroAtual < vetorQuadros.length) ? 1: 0;
			}
			vrTempo.reiniciaTempo();
		}
	}
	
	//Libera recursos
	public void liberaRecursos()
	{
		vetorQuadros = null;
	}
}
