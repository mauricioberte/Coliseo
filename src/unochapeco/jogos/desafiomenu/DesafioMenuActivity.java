/********************************************
 Programa: DesafioMenu
 Descri�‹o: apresenta implementacao de uma tela de Menu
 Autor: Silvano Maneck Malfatti
 Local: Unochapeco
 ********************************************/
//Pacote da aplicacao
package unochapeco.jogos.desafiomenu;

//Pacotes utilizados
import gameEngine.CGerenteEventos;
import gameEngine.CGerenteGrafico;
import gameEngine.CGerenteSons;
import gameEngine.CGerenteTempo;
import gameEngine.CIntervaloTempo;
import gameEngine.CNioBuffer;
import gameEngine.CQuadro;
import gameEngine.CSprite;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

//Classe que implementa a atividade principal da aplicacao Android
public class DesafioMenuActivity extends Activity {
	GLSurfaceView vrSuperficieDesenho = null;

	// Metodo chamado no momento da criacao da Activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vrSuperficieDesenho = new GLSurfaceView(this);

		// Preparacao da janela da aplicacao (modo tela cheia)
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Renderizador render = new Renderizador();
		render.setActivity(this);

		// Configurando a superficie de desenho
		vrSuperficieDesenho.setRenderer(render);

		// Registra a superficie de desenho ao tratador de eventos
		vrSuperficieDesenho.setOnTouchListener(CGerenteEventos.vrEventosTouch);

		// Ativa a superficie de desenho na janela
		setContentView(vrSuperficieDesenho);
	}

	// Metodo chamado no momento em que a Activity vai para segundo plano
	public void onPause() {
		super.onPause();
		vrSuperficieDesenho.onPause();
		CGerenteSons.vrMusica.pausaMusica();
	}

	// Metodo chamado no momento em que a Activity volta do segundo plano
	public void onResume() {
		super.onResume();
		vrSuperficieDesenho.onResume();
	}
}

class Renderizador implements Renderer {
	Random vrRand = null;
	// Constantes da classe
	final int ABERTURA = 0, MENU = 1, JOGO = 2, AJUDA = 3;

	// Atributos da classe
	int iLargura = 0, iAltura = 0;
	final int LARGURA_QUADRO = 2;
	final int ALTURA_QUADRO = 2;
	Activity vrActivity = null;
	int iEstado = ABERTURA;
	int iSubstado = 0;
	CIntervaloTempo vrTempo = null;
	CSprite vrSpriteSplash = null;
	CSprite vrSpriteFundoJogo = null;
	CSprite vrSpriteFundoAzul = null;
	CSprite vrSpriteTitulo = null;
	CSprite vrSpriteMenu = null;
	CSprite vrSpriteTitulo2 = null;
	CSprite vrSpriteTituloAjuda = null;
	CSprite vetBotoes[] = null;

	int vetCodigoSons[];
	int iJuliusEstado = 0;
	int iEstadoAtividade = 0;
	CSprite vrSpriteJulius = null;
	CSprite vrSpritechicotada = null;

	// // leoes
	// CSprite vrSpriteLeao3 = null;
	// CSprite vrQuadrosLeao3Desce = null;
	// CSprite vrQuadrosLeao3Sobe = null;
	//
	// // CSprite vetLeosDesce[] = null;
	// // CSprite vetLeosSobe[] = null;
	// CSprite vetLeos[] = null;
	// int iLeao0Estado = 0;
	// int iLeao1Estado = 0;
	// int iLeao2Estado = 0;
	// int iLeao3Estado = 0;
	// int iLeao4Estado = 0;

	ArrayList<CSprite> vetLeoes = null;
	CIntervaloTempo vrTempoCriacaoLeos = null;

	// Metodo utilizado para armazenar uma referencia da activity
	public void setActivity(Activity pActivity) {
		vrActivity = pActivity;
		CGerenteTempo.inicializaGerente();
		CGerenteGrafico.validaContexto(vrActivity);
		CGerenteSons.inicializa(vrActivity);
	}

	// Metodo chamado no momento em que a Activity e criada ou quando retorna do
	// estado Resume
	public void onSurfaceCreated(GL10 vrOpenGL, EGLConfig vrConfigueXo) {
		// Configura a cor de limpeza da tela
		vrOpenGL.glClearColor(0, 0, 0, 1);

		// Cria os codigos para os efeitos sonoros
		vetCodigoSons = new int[1];
		vetCodigoSons[0] = CGerenteSons.vrEfeitos.carregaSom("chicote1.wav");
	}

	// Metodo chamado quando tamanho da tela e alterado
	public void onSurfaceChanged(GL10 vrOpenGL, int pLargura, int pAltura) {
		// Armazena a altura e largura da tela
		iLargura = pLargura;
		iAltura = pAltura;

		// Configura a viewport
		vrOpenGL.glViewport(0, 0, iLargura, iAltura);

		// Configura a janela de visualizacao
		// Ativa a matriz de projecao para configura�‹o da janela de
		// visualizacao
		vrOpenGL.glMatrixMode(GL10.GL_PROJECTION);

		// Inicia a matriz de proje�‹o com a matriz identidade
		vrOpenGL.glLoadIdentity();

		// Seta as coordenadas da janela de visualizacao
		vrOpenGL.glOrthof(0.0f, iLargura, 0.0f, iAltura, 1.0f, -1.0f);

		// Inicia a matriz de desenho
		vrOpenGL.glMatrixMode(GL10.GL_MODELVIEW);

		// Inicia a matriz de desenho com a matriz identidade
		vrOpenGL.glLoadIdentity();

		// Configura a cor do desenho, habilita transparencia, uso de textura e
		// vetores de vertices texturas
		vrOpenGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		vrOpenGL.glEnable(GL10.GL_BLEND);
		vrOpenGL.glEnable(GL10.GL_ALPHA_TEST);
		vrOpenGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		vrOpenGL.glEnable(GL10.GL_TEXTURE_2D);
		vrOpenGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vrOpenGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Enviando os vertices ao OpenGL

		vrOpenGL.glVertexPointer(
				2,
				GL10.GL_FLOAT,
				0,
				CNioBuffer.geraNioBuffer(new float[] { -LARGURA_QUADRO / 2,
						-ALTURA_QUADRO / 2, -LARGURA_QUADRO / 2,
						ALTURA_QUADRO / 2, LARGURA_QUADRO / 2,
						-ALTURA_QUADRO / 2, LARGURA_QUADRO / 2,
						ALTURA_QUADRO / 2 }));

		// Inicializa um interalo de tempo de 3s
		vrTempo = new CIntervaloTempo();
		vrTempo.reiniciaTempo(300);

		vrSpriteSplash = new CSprite(vrOpenGL, R.drawable.splashscreen, 800,
				600, 800, 600);
		vrSpriteSplash.iPosX = iLargura / 2;
		vrSpriteSplash.iPosY = iAltura / 2;
		vrSpriteSplash.fEscalaX = 400;
		vrSpriteSplash.fEscalaY = 300;
		vrSpriteSplash.fAlpha = 0.0f;

		vrSpriteFundoAzul = new CSprite(vrOpenGL, R.drawable.fundoazul, 800,
				600, 800, 600);
		vrSpriteFundoAzul.iPosX = iLargura;
		vrSpriteFundoAzul.iPosY = iAltura;
		vrSpriteFundoAzul.fEscalaX = 400;
		vrSpriteFundoAzul.fEscalaY = 300;
		vrSpriteFundoAzul.fAlpha = 0.0f;

		vrSpriteFundoJogo = new CSprite(vrOpenGL, R.drawable.fundojogo, 800,
				600, 800, 600);
		vrSpriteFundoJogo.iPosX = iLargura / 2;
		vrSpriteFundoJogo.iPosY = iAltura / 2;
		vrSpriteFundoJogo.fEscalaX = 400;
		vrSpriteFundoJogo.fEscalaY = 300;

		vrSpriteTitulo = new CSprite(vrOpenGL, R.drawable.logo, 512, 256, 512,
				256);
		vrSpriteTitulo.iPosX = iLargura / 2;
		vrSpriteTitulo.iPosY = iAltura - 100;
		vrSpriteTitulo.fEscalaX = 144;
		vrSpriteTitulo.fEscalaY = 64;

		vrSpriteMenu = new CSprite(vrOpenGL, R.drawable.menuinicial, 800, 600,
				800, 600);
		vrSpriteMenu.iPosX = iLargura / 2;
		vrSpriteMenu.iPosY = iAltura / 2;
		vrSpriteMenu.fEscalaX = 400;
		vrSpriteMenu.fEscalaY = 300;

		vrSpriteTitulo2 = new CSprite(vrOpenGL, R.drawable.sair, 512, 256, 512,
				256);
		vrSpriteTitulo2.iPosX = iLargura / 2;
		vrSpriteTitulo2.iPosY = iAltura - 100;
		vrSpriteTitulo2.fEscalaX = 256;
		vrSpriteTitulo2.fEscalaY = 124;

		vrSpriteTituloAjuda = new CSprite(vrOpenGL, R.drawable.logo, 512, 256,
				512, 256);
		vrSpriteTituloAjuda.iPosX = iLargura / 2;
		vrSpriteTituloAjuda.iPosY = iAltura - 400;
		vrSpriteTituloAjuda.fEscalaX = 256;
		vrSpriteTituloAjuda.fEscalaY = 124;

		// Cria um Sprite com animacao
		CQuadro[] vrQuadrosJulius = new CQuadro[5];
		for (int iIndex = 0; iIndex < 5; iIndex++) {
			vrQuadrosJulius[iIndex] = new CQuadro(iIndex + 7);
		}
		CQuadro[] vrQuadrosJuliusPara = new CQuadro[2];
		for (int iIndex = 0; iIndex < 2; iIndex++) {
			vrQuadrosJuliusPara[iIndex] = new CQuadro(iIndex + 13);
		}

		// CQuadro[] vrQuadrosHidrante2 = new CQuadro[10];
		// for (int iIndex=0; iIndex < vrQuadrosHidrante2.length; iIndex++)
		// {
		// vrQuadrosHidrante2[iIndex] = new CQuadro(20+iIndex);
		// }
		vrSpriteJulius = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192,
				256);
		vrSpriteJulius.iEspelhamento = CSprite.HORIZONTAL;
		vrSpriteJulius.iPosX = 10;
		vrSpriteJulius.iPosY = 150;
		vrSpriteJulius.fEscalaX = 16;
		vrSpriteJulius.fEscalaY = 32;
		vrSpriteJulius.criaAnimacao(30, true, vrQuadrosJulius);
		vrSpriteJulius.criaAnimacao(1, true, vrQuadrosJuliusPara);
		vrSpriteJulius.configuraAnimcaceoAtual(0);

		// // leao 3
		// CQuadro[] vrQuadrosLeao3Desce = new CQuadro[6];
		// for (int iIndex = 0; iIndex < 6; iIndex++) {
		// vrQuadrosLeao3Desce[iIndex] = new CQuadro(iIndex);
		// }
		// CQuadro[] vrQuadrosLeao3Sobe = new CQuadro[6];
		// for (int iIndex = 0; iIndex < 6; iIndex++) {
		// vrQuadrosLeao3Sobe[iIndex] = new CQuadro(iIndex + 6);
		// }
		//
		// vrSpriteLeao3 = new CSprite(vrOpenGL, R.drawable.leao3, 72, 118, 432,
		// 238);
		// vrSpriteLeao3.iPosX = iLargura / 2;
		// vrSpriteLeao3.iPosY = iAltura - 170;
		// vrSpriteLeao3.fEscalaX = 18;
		// vrSpriteLeao3.fEscalaY = 29;
		// vrSpriteLeao3.criaAnimacao(10, true, vrQuadrosLeao3Desce);
		// vrSpriteLeao3.criaAnimacao(30, true, vrQuadrosLeao3Sobe);
		// vrSpriteLeao3.configuraAnimcaceoAtual(0);
		// vrSpriteLeao3.fAlpha = 0;
		//
		// // Cria o vetor de sprites
		// vetLeos = new CSprite[5];
		// for (int i = 0; i < vetLeos.length; i++) {
		// // // leao 3
		// // CQuadro[] vrQuadrosLeao3Desce = new CQuadro[6];
		// // for (int iIndex = 0; iIndex < 6; iIndex++) {
		// // vrQuadrosLeao3Desce[iIndex] = new CQuadro(iIndex);
		// // }
		// // CQuadro[] vrQuadrosLeao3Sobe = new CQuadro[6];
		// // for (int iIndex = 0; iIndex < 6; iIndex++) {
		// // vrQuadrosLeao3Sobe[iIndex] = new CQuadro(iIndex + 6);
		// // }
		// vetLeos[i] = new CSprite(vrOpenGL, R.drawable.leao3, 72, 118, 432,
		// 238);
		// vetLeos[i].fEscalaX = 18;
		// vetLeos[i].fEscalaY = 29;
		// vetLeos[i].criaAnimacao(10, true, vrQuadrosLeao3Desce);
		// vetLeos[i].criaAnimacao(30, true, vrQuadrosLeao3Sobe);
		// vetLeos[i].configuraAnimcaceoAtual(0);
		// vetLeos[i].fAlpha = 1;
		// }
		//
		// vetLeos[0].iPosX = 0;
		// vetLeos[0].iPosY = 500;
		// vetLeos[1].iPosX = 130;
		// vetLeos[1].iPosY = 500;
		// vetLeos[2].iPosX = 280;
		// vetLeos[2].iPosY = 500;
		// vetLeos[3].iPosX = 430;
		// vetLeos[3].iPosY = 500;
		// vetLeos[4].iPosX = 580;
		// vetLeos[4].iPosY = 500;

		// Cria um Sprite com animacao
		CQuadro[] vrQuadrosChicotada = new CQuadro[3];
		for (int iIndex = 0; iIndex < 3; iIndex++) {
			vrQuadrosChicotada[iIndex] = new CQuadro(iIndex + 20);
		}
		vrSpritechicotada = new CSprite(vrOpenGL, R.drawable.briga, 64, 64,
				512, 256);
		vrSpritechicotada.iPosX = iLargura / 2;
		vrSpritechicotada.iPosY = iAltura / 2;
		vrSpritechicotada.fEscalaX = 24;
		vrSpritechicotada.fEscalaY = 24;
		vrSpritechicotada.criaAnimacao(10, false, vrQuadrosChicotada);

		// Cria o vetor de sprites
		vetBotoes = new CSprite[3];
		vetBotoes[0] = new CSprite(vrOpenGL, R.drawable.jogar, 128, 42, 128,
				128);
		vetBotoes[1] = new CSprite(vrOpenGL, R.drawable.ajuda, 128, 42, 128,
				128);
		vetBotoes[2] = new CSprite(vrOpenGL, R.drawable.sair, 128, 42, 128, 128);

		// Cria os quadros da animacao
		CQuadro[] vrQuadro0 = new CQuadro[1];
		vrQuadro0[0] = new CQuadro(0);
		CQuadro[] vrQuadro1 = new CQuadro[1];
		vrQuadro1[0] = new CQuadro(1);
		CQuadro[] vrQuadro2 = new CQuadro[1];
		vrQuadro2[0] = new CQuadro(2);

		// Configura a escala, as animacoes e posicao de cada botao
		for (int iIndex = 0; iIndex < 3; iIndex++) {
			vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro0);
			vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro1);
			vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro2);
			vetBotoes[iIndex].iPosX = iLargura / 2;
			vetBotoes[iIndex].iPosY = iAltura - 270 - iIndex * 50;
			vetBotoes[iIndex].fEscalaX = 64;
			vetBotoes[iIndex].fEscalaY = 21;
			vetBotoes[iIndex].configuraAnimcaceoAtual(0);
		}

		vetLeoes = new ArrayList<CSprite>();

		// Cria o objeto temporizador para criacao de asteroides e controle
		// tempo de tiro
		vrTempoCriacaoLeos = new CIntervaloTempo();
		vrTempoCriacaoLeos.reiniciaTempo(1000);

		// Cria o objeto randomizador
		vrRand = new Random();

	}

	// Metodo chamado quempre que possivel para realizar o desenho grafico na
	// superficie
	public void onDrawFrame(GL10 vrOpenGL) {
		// Limpa o fundo da tela
		vrOpenGL.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Gerencia os estados do jogo
		if (iEstado == ABERTURA) {
			abertura();
		} else if (iEstado == MENU) {
			menu();
		} else if (iEstado == AJUDA) {
			ajuda();
		} else if (iEstado == JOGO) {
			JuliusAnda(vrOpenGL);
		}

		// Executa as etapas de um jogo
		CGerenteTempo.atualiza();
		CGerenteEventos.vrEventosTouch.atualizaEstados();

		// Pausa no loop da aplicacao
		pausa();
	}

	private void iniciaJogo(GL10 vrOpenGL) {
		// Gerencia os asteroies
		atualiLeoes(vrOpenGL);
		// atualizaExplosoes();

		// Atualiza jogador
		// defineDirecaoJogador();
		// atualizaPosicaoJogador();

		// Desenha jogador
		atualizatelaJogo();
		atualizaPersonagem();
		// vrNave.atualizaSprite();
		// vrNave.desenhaSprite();

		// Realiza o desenho

		desenhaLeoes();

		if (CGerenteEventos.vrEventosTouch.telaClicada() == true) {
			vrSpritechicotada.iPosX = (int) CGerenteEventos.vrEventosTouch.fPosX;
			vrSpritechicotada.iPosY = iAltura
					- ((int) CGerenteEventos.vrEventosTouch.fPosY);
			CGerenteSons.vrEfeitos.reproduzSom(1);
			chicotada();
		}
	}

	void desenhaLeoes() {
		// Atualiza os sprites
		for (int iIndex = 0; iIndex < vetLeoes.size(); iIndex++) {
			vetLeoes.get(iIndex).atualizaSprite();
			vetLeoes.get(iIndex).desenhaSprite();
		}
	}

	// Cria um sprite de meteoro quando necessario
	CSprite criaLeoes(GL10 vrOpenGL) {
		CSprite vrLeoes = null;

		CQuadro[] vrQuadrosLeaoDesce = new CQuadro[6];
		 for (int iIndex = 0; iIndex < 6; iIndex++) {
		 vrQuadrosLeaoDesce[iIndex] = new CQuadro(iIndex);
		 }
		 CQuadro[] vrQuadrosLeaoSobe = new CQuadro[6];
		 for (int iIndex = 0; iIndex < 6; iIndex++) {
		 vrQuadrosLeaoSobe[iIndex] = new CQuadro(iIndex + 6);
		 }

		// Cria o Sprite

		vrLeoes = new CSprite(vrOpenGL, R.drawable.leao3, 72, 118, 432, 238);
		int posicao = vrRand.nextInt(5);
		switch (posicao) {
		case 0:
			vrLeoes.iPosX = iLargura-135;
			break;
		case 1:
			vrLeoes.iPosX = 135;
			break;
		case 2:
			vrLeoes.iPosX = 265;
			break;
		case 3:
			vrLeoes.iPosX = 395;
			break;
		case 4:
			vrLeoes.iPosX = 530;
			break;
		case 5:
			vrLeoes.iPosX = 680;
			break;

		}
//		vrLeoes.iPosX = vrRand.nextInt(iLargura);

		vrLeoes.iPosY = iAltura -180;
		vrLeoes.iDirY = -1;
		vrLeoes.iDirX = (vrRand.nextInt(2) == 0) ? -1 : 1;
		vrLeoes.fEscalaX = 18;
		vrLeoes.fEscalaY = 29;
		vrLeoes.iEspelhamento = (vrLeoes.iDirX > 0) ? CSprite.NORMAL
				: CSprite.HORIZONTAL;
		vrLeoes.criaAnimacao(10, true, vrQuadrosLeaoDesce);
		vrLeoes.criaAnimacao(30, true, vrQuadrosLeaoSobe);

		return vrLeoes;
	}

	private void atualiLeoes(GL10 vrOpenGL) {
		// verifica se ja e hora de criar o sprite
		vrTempoCriacaoLeos.atualiza();
		if (vrTempoCriacaoLeos.tempoFinalizado()) {
			vetLeoes.add(criaLeoes(vrOpenGL));
			vrTempoCriacaoLeos.reiniciaTempo(2000);
		}

		// Atualiza a pos dos asteroides e remove os asteroies que ja sairam da
		// tela
		for (int iIndex = vetLeoes.size() - 1; iIndex >= 0; iIndex--) {
			

			if ((vetLeoes.get(iIndex).iPosY >= iAltura-180)&&(vetLeoes.get(iIndex).estado ==false)) {
				vetLeoes.remove(iIndex);
			}
			
			if (vetLeoes.get(iIndex).iPosY < 180){
				vetLeoes.get(iIndex).estado = false;
			}
			
			if (vetLeoes.get(iIndex).estado) {
				vetLeoes.get(iIndex).iPosY -= 10;
				vetLeoes.get(iIndex).iPosX += vetLeoes.get(iIndex).iDirX * 2;
			}else{
				vetLeoes.get(iIndex).iPosY += 10;
				vetLeoes.get(iIndex).iPosX += vetLeoes.get(iIndex).iDirX * 2;
				 vetLeoes.get(iIndex).configuraAnimcaceoAtual(1);

			}
		}

	}

	// Metodo utilizado para realizar uma pausa no loop da aplicacao
	private void pausa() {
		try {
			Thread.sleep(50);
		} catch (Exception e) {

		}
	}

	// Metodo que gerencia o estado de abertura
	public void abertura() {
		vrTempo.atualiza();
		if (iSubstado == 0) {
			if (vrTempo.tempoFinalizado()) {
				vrSpriteSplash.fAlpha += 0.02;
				if (vrSpriteSplash.fAlpha >= 1.0f) {
					iSubstado = 1;
				}
			}
		} else {
			vrSpriteSplash.fAlpha -= 0.03f;
			if (vrSpriteSplash.fAlpha <= 0.0f) {
				// Carrega uma musica de fundo
				CGerenteSons.vrMusica.carregaMusica("menu.mid", true);
				CGerenteSons.vrMusica.reproduzMusica();

				iEstado = MENU;
			}
		}
		vrSpriteSplash.atualizaSprite();
		vrSpriteSplash.desenhaSprite();
	}

	// Metodo que gerencia o estado do Menu
	public void menu() {
		vrSpriteMenu.atualizaSprite();
		vrSpriteMenu.desenhaSprite();

		// Verifica se houve toque no botao e configura as animacoes
		for (int iIndex = 0; iIndex < 3; iIndex++) {
			if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_DOWN
					|| CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_MOVE) {
				if (vetBotoes[iIndex].colidePonto(
						(int) CGerenteEventos.vrEventosTouch.fPosX, iAltura
								- (int) CGerenteEventos.vrEventosTouch.fPosY)) {
					if (vetBotoes[iIndex].iQuadroAtual != 1) {
						vetBotoes[iIndex].configuraAnimcaceoAtual(1);
						CGerenteSons.vrEfeitos.reproduzSom(vetCodigoSons[0]);
					}
				} else {
					vetBotoes[iIndex].configuraAnimcaceoAtual(0);
				}
			} else if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_UP) {
				if (vetBotoes[iIndex].colidePonto(
						(int) CGerenteEventos.vrEventosTouch.fPosX, iAltura
								- (int) CGerenteEventos.vrEventosTouch.fPosY)) {
					if (vetBotoes[iIndex].iQuadroAtual != 0) {
						vetBotoes[iIndex].configuraAnimcaceoAtual(0);
						if (iIndex == 1) {
							iEstado = AJUDA;
						}
						if (iIndex == 2) {
							vrActivity.finish();
						}
						if (iIndex == 0) {
							CGerenteSons.vrMusica.carregaMusica("rugido.wav",
									false);
							CGerenteSons.vrMusica.reproduzMusica();
							iEstado = JOGO;
						}

					}
				}
			}

			// Atualiza sprites
			vetBotoes[iIndex].atualizaSprite();
			vetBotoes[iIndex].desenhaSprite();

		}
	}

	public void ajuda() {
		Log.i(null, "Foi");
		vrSpriteTitulo2.atualizaSprite();
		vrSpriteTitulo2.desenhaSprite();

		vrSpriteTituloAjuda.atualizaSprite();
		vrSpriteTituloAjuda.desenhaSprite();
	}

	// public void jogo() {
	// atualizatelaJogo();
	// atualizaPersonagem();
	//
	// for (int i = 0; i < vetLeos.length; i++) {
	// atualizaLeao(i);
	// }
	//
	// if (iEstadoAtividade == 0) {
	// JuliusAnda();
	// } else {
	// if (vrSpritechicotada.retornaAnimacaoAtual().animacaoFinalizada()) {
	// vrSpritechicotada.reiniciaAnimacao();
	// iEstadoAtividade = 0;
	// }
	// vrSpritechicotada.atualizaSprite();
	// vrSpritechicotada.desenhaSprite();
	// }
	// if (iJuliusEstado == 1) {
	// if (CGerenteEventos.vrEventosTouch.telaClicada() == true) {
	// vrSpritechicotada.iPosX = (int) CGerenteEventos.vrEventosTouch.fPosX;
	// vrSpritechicotada.iPosY = iAltura
	// - ((int) CGerenteEventos.vrEventosTouch.fPosY);
	// CGerenteSons.vrEfeitos.reproduzSom(1);
	// System.out.println(1234);
	//
	// chicotada();
	// }
	// leaoAnda3();
	// }
	// }

	// private void leaoAnda3() {
	// for (int i = 0; i < vetLeos.length; i++) {
	// if (iLeao3Estado == 0) {
	// vetLeos[i].iPosY -= 3;
	// // vrSpriteLeao3.iEspelhamento = 1;
	//
	// if (vetLeos[i].iPosY <= 180) {
	// iLeao3Estado = 1;
	// vetLeos[i].configuraAnimcaceoAtual(1);
	//
	// }
	// if (vetLeos[i].iPosY <= iAltura - 180) {
	// if (vetLeos[i].fAlpha <= 1)
	// vetLeos[i].fAlpha += 0.5f;
	// }
	// //
	// } else {
	// iLeao3Estado = 1;
	// vetLeos[i].iPosY += 6;
	// if (vetLeos[i].iPosY >= iAltura - 180) {
	// if (vetLeos[i].fAlpha >= 0.4)
	// vetLeos[i].fAlpha -= 0.5f;
	// }
	// // System.out.println(vrRand.nextInt(600));
	// if (vetLeos[i].iPosY >= (iAltura - 150)) {
	// iLeao3Estado = 0;
	// vetLeos[i].configuraAnimcaceoAtual(0);
	// }
	//
	// // vrSpriteJulius.iEspelhamento = 1;
	// //
	// // if (vrSpriteJulius.iPosX >= iLargura - 50) {
	// // iJuliusEstado = 0;
	// // }
	// }
	// }
	//
	// }

	private void JuliusAnda(GL10 vrOpenGL) {
		// if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual ==
		// MotionEvent.ACTION_DOWN) {
		// if (iJuliusEstado == 0) {
		// iJuliusEstado = 1;
		// } else {
		// iJuliusEstado = 0;
		// }
		// }

		if (iJuliusEstado == 0) {
			vrSpriteJulius.iPosX += 5;
			vrSpriteJulius.iEspelhamento = 1;

			if (vrSpriteJulius.iPosX >= iLargura / 2) {
				iJuliusEstado = 1;
				vrSpriteJulius.configuraAnimcaceoAtual(1);

			}
			atualizatelaJogo();
			atualizaPersonagem();
			// if (vrSpriteGato.colide(vrSpriteJulius.iPosX,
			// vrSpriteJulius.iPosY,
			// vrSpriteJulius.iLarguraQuadro * vrSpriteJulius.fEscalaX,
			// vrSpriteJulius.iAlturaQuadro * vrSpriteJulius.fEscalaY))
			// {
			// vrSpriteBriga.reiniciaAnimacao();
			// iJuliusEstado = 1;
			// }
		} else {
			iJuliusEstado = 1;
			// vrSpriteJulius.iPosX += 5;
			// vrSpriteJulius.iEspelhamento = 1;
			//
			// if (vrSpriteJulius.iPosX >= iLargura - 50) {
			// iJuliusEstado = 0;
			// }
			iniciaJogo(vrOpenGL);
		}

	}

	private void chicotada() {
		for (int i = 0; i < vetLeoes.size(); i++) {
			if (vetLeoes.get(i).colidePonto(vrSpritechicotada.iPosX,
					vrSpritechicotada.iPosY)) {
				// torcaEstadoLeao(i);
				// iLeao3Estado = 1;
				 vetLeoes.get(i).configuraAnimcaceoAtual(1);
				 vetLeoes.get(i).estado=false;

			}

			// iEstadoAtividade = 1;
		}
	}

	//
	// private void torcaEstadoLeao(int i) {
	// switch (i) {
	// case 0:
	// iLeao0Estado *= -1;
	// break;
	// case 1:
	// iLeao1Estado *= -1;
	// break;
	// case 2:
	// iLeao2Estado *= -1;
	// break;
	// case 3:
	// iLeao3Estado *= -1;
	// break;
	// case 4:
	// iLeao4Estado *= -1;
	// break;
	// }
	//
	// }

	// private void atualizaLeao(int i) {
	// vetLeos[i].atualizaSprite();
	// vetLeos[i].desenhaSprite();
	// }

	private void atualizaPersonagem() {
		vrSpriteJulius.atualizaSprite();
		vrSpriteJulius.desenhaSprite();
	}

	private void atualizatelaJogo() {
		vrSpriteFundoJogo.atualizaSprite();
		vrSpriteFundoJogo.desenhaSprite();
	}
}