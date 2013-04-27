package gameEngine;

import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class CGerenteGrafico 
{
	//Atributos da classe
	public static Activity vrActivity=null;
	
	//Metodo utilizado para setar a referencia ao contexo
	public static void validaContexto(Activity pActivity)
	{
		vrActivity = pActivity;
	}
	
	//Metodo utilizado para carregar uma imagem
	public static int carregaImagem(int iCodigo, GL10 vrOpenGL)
	{
		//Passo 1 - carrega a textura
		int[] vetTexturas = new int[1];
		Bitmap vrImagem  = BitmapFactory.decodeResource(CGerenteGrafico.vrActivity.getResources(), iCodigo);
				
		//Passo 2 - define o codigo para a textura
		vrOpenGL.glGenTextures(1, vetTexturas, 0);
				
		//Passo 3 - Carrega a imagem na area de memoria da VRAM representada pelo codigo da textura gerado previamente
		vrOpenGL.glBindTexture(GL10.GL_TEXTURE_2D, vetTexturas[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, vrImagem, 0);
				
				
		//Passo4 - Define os filtros de textura
		vrOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		vrOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
									
		//Passo 5 - Sai do modo textura e libera a imagem do Bitmap na RAM
		vrOpenGL.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		vrImagem.recycle();
		
		return vetTexturas[0];
	}
}
