//Pacote da classe
package gameEngine;

//Pacotes utilizados
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class CMusica 
{
	MediaPlayer vrMedia = null;
	Activity vrContexto = null;
	
	//Construtor da classe
	public CMusica(Activity pActivity)
	{
		vrContexto = pActivity;
		vrMedia = new MediaPlayer();
	}

	//Metodo utilizado para carregar a musica solicitada com ou sem repeticao
	public void carregaMusica(String sNomeMusica, boolean bRepete)
	{
		AssetFileDescriptor vrDescritor = null;
		
		try
		{
			vrDescritor = vrContexto.getAssets().openFd(sNomeMusica);
			vrMedia.setDataSource(vrDescritor.getFileDescriptor(), vrDescritor.getStartOffset(), vrDescritor.getLength());
			vrMedia.prepare();
			vrMedia.setLooping(bRepete);
		}
		catch(Exception e)
		{
			
		}
	}
	
	//Metodo utilizado para iniciar a reproducao da musica
	public void reproduzMusica()
	{
		vrMedia.start();
	}
	
	//Metodo utilizado para pausar a reproducao da musica
	public void pausaMusica()
	{
		vrMedia.pause();
	}
	
	//Metodo utilizado para setar o volume nos canais esquerdo e direito
	public void setVolume(int iEsquerdo, int iDireito)
	{
		vrMedia.setVolume(iEsquerdo, iDireito);
	}
	
	//Metodo utilizado para descobrir se a musica esta ou nao em reproducao
	public boolean emReproducao()
	{
		return vrMedia.isPlaying();
	}
	
	//Metodo utilizado para liberar recursos
	public void liberaRecursos()
	{
		vrMedia.stop();
		vrMedia.release();
		vrMedia = null;
	}
}
