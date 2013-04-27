//Pacote da classe
package gameEngine;

//Pacotes utilizados
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;


public class CEfeitoSom
{
	//Atributos da classe
	SoundPool vrPool = null;
	Activity vrContexto = null;
	
	//Construtor da classe
	public CEfeitoSom(Activity pActivity)
	{
		vrContexto = pActivity;
		vrPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}
	
	//Metodo utilizado para o carregamento de um efeito
	public int carregaSom(String sNomeSom)
	{
		AssetFileDescriptor vrDescritor = null;
		
		try
		{
			vrDescritor = vrContexto.getAssets().openFd(sNomeSom);
		}
		catch(Exception e)
		{
			
		}
		
		return vrPool.load(vrDescritor, 1);
	}
	
	//Metodo utilizado para disparar um som
	public void reproduzSom(int iCodSom)
	{
		vrPool.play(iCodSom, 1.0f, 1.0f, 0, 0, 1);
	}
	
	//Metodo utilizado para descarregar um som
	public void descarregaSom(int iCodSom)
	{
		vrPool.unload(iCodSom);
	}
}
