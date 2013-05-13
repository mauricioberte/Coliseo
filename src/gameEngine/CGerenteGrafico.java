package gameEngine;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

//Classe local utilizada para armazenar dados de textura
class CDadosTextura
{
	//Atributos da classe
	int iReferencia = 0;
	int iCodTextura = 0;
	
	//Construtor da classe
	CDadosTextura(int pRef, int pCod)
	{
		iReferencia = pRef;
		iCodTextura = pCod;
	}
}

public class CGerenteGrafico 
{
	//Atributos da classe
	public static Activity vrActivity=null;
	public static ArrayList<CDadosTextura> vetListaTexturas = null;
	
	//Metodo utilizado para setar a referencia ao contexo
	public static void validaContexto(Activity pActivity)
	{
		vrActivity = pActivity;
	}
	
	//Metodo utilizado para carregar uma imagem
	public static int carregaImagem(int iReferencia, GL10 vrOpenGL)
	{
		//Verifica se lista ja foi criada
		if (vetListaTexturas == null)
		{
			vetListaTexturas = new ArrayList<CDadosTextura>();
		}
		
		//Verifica se a textura ja foi carregada
		for (int iIndex=0; iIndex < vetListaTexturas.size(); iIndex++)
		{
			if (iReferencia == vetListaTexturas.get(iIndex).iReferencia)
			{
				return vetListaTexturas.get(iIndex).iCodTextura;
			}
		}
		
		//Passo 1 - carrega a textura
		int[] vetTexturas = new int[1];
		Bitmap vrImagem  = BitmapFactory.decodeResource(CGerenteGrafico.vrActivity.getResources(), iReferencia);
				
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
		
		//Adiciona nova textura na lista
		vetListaTexturas.add(new CDadosTextura(iReferencia, vetTexturas[0]));
		
		return vetTexturas[0];
	}
	
	
	//Libera recursos
	public static void release()
	{
		vetListaTexturas.clear();
	}
}
