/********************************************
 Programa: Exemplo14
 Descrição: apresenta a reprodução de sons
 Autor: Silvano Maneck Malfatti
 Local: Unochapeco
 ********************************************/
package gameEngine;

//Pacotes utilizados
import android.app.Activity;
import android.media.AudioManager;

public class CGerenteSons 
{
	//Atributos da classe
	public static CEfeitoSom vrEfeitos = null;
	public static CMusica vrMusica = null;
	
	//Meotodo utilizado para inicializacao de recursos
	public static void inicializa(Activity vrActivity)
	{
		//Atribui ao Android o controle da aplicacao
		vrActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Cria a instancia do controlador de efeitos
		vrEfeitos = new CEfeitoSom(vrActivity);
		
		//Cria e instancia o controlador de musicas
		vrMusica = new CMusica(vrActivity);
	}
}
