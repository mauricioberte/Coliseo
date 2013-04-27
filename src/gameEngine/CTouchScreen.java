/********************************************
 Programa: Exemplo14
 Descrição: apresenta captura e tratamento de eventos touch
 Autor: Silvano Maneck Malfatti
 Local: Unochapeco
 ********************************************/

//Pacote da aplicacao
package gameEngine;

//Pacotes utilizados
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CTouchScreen implements OnTouchListener
{
	//Atributos da classe
	public float fPosX = 0, fPosY = 0;
	int iTipoEventoAnterior = 0;
	public int iTipoEventoAtual = 0;
	
	//Metodo utilizado para capturar as informacoes do evento ocorrido
	public boolean onTouch(View vrComponente, MotionEvent vrEventoMovimento)
	{
		//Armazena os dados do ultimo evento gerado
		iTipoEventoAnterior = iTipoEventoAtual;
		iTipoEventoAtual = vrEventoMovimento.getAction();
		fPosX = vrEventoMovimento.getX();
		fPosY = vrEventoMovimento.getY();
		
		return true;
	}
	
	//Metodo utilizado para identificar um clique
	public boolean telaClicada()
	{
		//Verifica os estados para ver se a tela foi clicada
		if (iTipoEventoAtual == MotionEvent.ACTION_UP && (iTipoEventoAnterior == MotionEvent.ACTION_DOWN || iTipoEventoAnterior == MotionEvent.ACTION_MOVE)) 
		{
			return true;
		}
		
		return false;
	}
	
	//Metodo utilizado para atualizar os estados do touch
	public void atualizaEstados()
	{
		iTipoEventoAnterior = iTipoEventoAtual ;
	}
	
}
