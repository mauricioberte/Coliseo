//Pacote da aplicacao
package br.com.noisquefez.coliseo;

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
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

//Classe que implementa a atividade principal da aplicacao Android
public class ColiseoActivity extends Activity {
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
		CGerenteGrafico.release();
		CGerenteSons.vrMusica.pausaMusica();
	}

	// Metodo chamado no momento em que a Activity volta do segundo plano
	public void onResume() {
		super.onResume();
		vrSuperficieDesenho.onResume();
		CGerenteSons.vrMusica.reproduzMusica();

	}
}

class Renderizador implements Renderer {
	Random vrRand = null;
	// Constantes da classe
	final int ABERTURA = 0, MENU = 1, JOGO = 2, AJUDA = 3;
	boolean finalizaJogo = false;
	boolean pausaJogo = false;

	// Atributos da classe
	int iLargura = 0, iAltura = 0;
	final int LARGURA_QUADRO = 2;
	final int ALTURA_QUADRO = 2;
	Activity vrActivity = null;
	int iEstado = ABERTURA;
	int iSubstado = 0;
	CIntervaloTempo vrTempo = null;
	CSprite vrSpriteSplash = null;
	CSprite vrSpriteNumeros = null;
	CSprite vrSpritePontuacao = null;
	CSprite vrSpriteFundoJogo = null;
	CSprite vrSpriteFundoAzul = null;
	CSprite vrSpriteTitulo = null;
	CSprite vrSpriteMenu = null;
	CSprite vrSpriteTitulo2 = null;
	CSprite vrSpriteTituloAjuda = null;
	CSprite vrSpriteTelaFinalGamoOver = null;
	CSprite vrSpriteTituloPausa = null;
	CSprite vrSpriteTelaFinal = null;
	CSprite vetBotoes[] = null;
	CSprite vetVidas[] = null;

	CSprite btnPausa = null;

	int vetCodigoSons[];
	int iJuliusEstado = 0;
	int iEstadoAtividade = 0;
	CSprite vrSpriteJulius = null;
	CSprite vrSpritechicotada = null;

	int leaoMorto = 0;

	int unidade = 0;
	int dezena = 0;
	int centena = 0;
	int vida = 5;

	int tempoCriacaoLeoes = 3000;

	int temppoDePausa = 10;

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
		vetCodigoSons = new int[3];
		vetCodigoSons[0] = CGerenteSons.vrEfeitos.carregaSom("chicote1.wav");
		vetCodigoSons[1] = CGerenteSons.vrEfeitos.carregaSom("rugido.wav");
		vetCodigoSons[2] = CGerenteSons.vrEfeitos.carregaSom("grito1.wav");

	}

	// Metodo chamado quando tamanho da tela e alterado
	public void onSurfaceChanged(GL10 vrOpenGL, int pLargura, int pAltura) {
		// Armazena a altura e largura da tela
		iLargura = pLargura;
		iAltura = pAltura;
		iJuliusEstado = 0;

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

		vrSpriteSplash = new CSprite(vrOpenGL, R.drawable.splashscreen,
				iLargura, iAltura, iLargura, iAltura);
		vrSpriteSplash.iPosX = iLargura / 2;
		vrSpriteSplash.iPosY = iAltura / 2;
		vrSpriteSplash.fEscalaX = iLargura / 2;
		vrSpriteSplash.fEscalaY = iAltura / 2;
		vrSpriteSplash.fAlpha = 0.0f;

		vrSpriteFundoAzul = new CSprite(vrOpenGL, R.drawable.fundoazul,
				iLargura, iAltura, iLargura, iAltura);
		vrSpriteFundoAzul.iPosX = iLargura;
		vrSpriteFundoAzul.iPosY = iAltura;
		vrSpriteFundoAzul.fEscalaX = iLargura / 2;
		vrSpriteFundoAzul.fEscalaY = iAltura / 2;
		vrSpriteFundoAzul.fAlpha = 0.0f;

		vrSpriteFundoJogo = new CSprite(vrOpenGL, R.drawable.fundojogo,
				iLargura, iAltura, iLargura, iAltura);
		vrSpriteFundoJogo.iPosX = iLargura / 2;
		vrSpriteFundoJogo.iPosY = iAltura / 2;
		vrSpriteFundoJogo.fEscalaX = iLargura / 2;
		vrSpriteFundoJogo.fEscalaY = iAltura / 2;

		vrSpriteTitulo = new CSprite(vrOpenGL, R.drawable.logo, 512, 256, 512,
				256);
		vrSpriteTitulo.iPosX = iLargura / 2;
		vrSpriteTitulo.iPosY = iAltura - 100;
		vrSpriteTitulo.fEscalaX = 144;
		vrSpriteTitulo.fEscalaY = 64;

		vrSpriteMenu = new CSprite(vrOpenGL, R.drawable.menuinicial, iLargura,
				iAltura, iLargura, iAltura);
		vrSpriteMenu.iPosX = iLargura / 2;
		vrSpriteMenu.iPosY = iAltura / 2;
		vrSpriteMenu.fEscalaX = iLargura / 2;
		vrSpriteMenu.fEscalaY = iAltura / 2;

		vrSpriteTitulo2 = new CSprite(vrOpenGL, R.drawable.sair, 512, 256, 512,
				256);
		vrSpriteTitulo2.iPosX = iLargura / 2;
		vrSpriteTitulo2.iPosY = iAltura - 100;
		vrSpriteTitulo2.fEscalaX = 256;
		vrSpriteTitulo2.fEscalaY = 124;

		vrSpriteTituloAjuda = new CSprite(vrOpenGL, R.drawable.creditos,
				iLargura, iAltura, iLargura, iAltura);
		vrSpriteTituloAjuda.iPosX = iLargura / 2;
		vrSpriteTituloAjuda.iPosY = iAltura / 2;
		vrSpriteTituloAjuda.fEscalaX = 400;
		vrSpriteTituloAjuda.fEscalaY = 300;

		vrSpriteTituloPausa = new CSprite(vrOpenGL, R.drawable.pausa, iLargura,
				iAltura, iLargura, iAltura);
		vrSpriteTituloPausa.iPosX = iLargura / 2;
		vrSpriteTituloPausa.iPosY = iAltura / 2;
		vrSpriteTituloPausa.fEscalaX = 400;
		vrSpriteTituloPausa.fEscalaY = 300;

		// Cria um Sprite com animacao
		CQuadro[] vrQuadrosJulius = new CQuadro[5];
		for (int iIndex = 0; iIndex < 5; iIndex++) {
			vrQuadrosJulius[iIndex] = new CQuadro(iIndex + 7);
		}
		CQuadro[] vrQuadrosJuliusPara = new CQuadro[2];
		for (int iIndex = 0; iIndex < 2; iIndex++) {
			vrQuadrosJuliusPara[iIndex] = new CQuadro(iIndex + 13);
		}

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

		// Cria um Sprite com animacao
		CQuadro[] vrQuadrosChicotada = new CQuadro[3];
		for (int iIndex = 0; iIndex < 3; iIndex++) {
			vrQuadrosChicotada[iIndex] = new CQuadro(iIndex + 20);
		}

		CQuadro[] vrQuadrosMordida = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosMordida[iIndex] = new CQuadro(iIndex);
		}
		vrSpritechicotada = new CSprite(vrOpenGL, R.drawable.briga, 64, 64,
				512, 256);
		vrSpritechicotada.iPosX = iLargura / 2;
		vrSpritechicotada.iPosY = iAltura / 2;
		vrSpritechicotada.fEscalaX = 24;
		vrSpritechicotada.fEscalaY = 24;
		vrSpritechicotada.criaAnimacao(15, false, vrQuadrosMordida);
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
			vetBotoes[iIndex].iPosY = iAltura - iAltura / 2 - iIndex * 50;
			vetBotoes[iIndex].fEscalaX = 64;
			vetBotoes[iIndex].fEscalaY = 21;
			vetBotoes[iIndex].configuraAnimcaceoAtual(0);
		}

		// Cria o vetor de sprites
		vetVidas = new CSprite[vida];

		// Cria os quadros da animacao
		CQuadro[] vrQuadroVidas = new CQuadro[1];
		vrQuadroVidas[0] = new CQuadro(0);

		vetVidas[0] = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192, 256);
		vetVidas[1] = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192, 256);
		vetVidas[2] = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192, 256);
		vetVidas[3] = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192, 256);
		vetVidas[4] = new CSprite(vrOpenGL, R.drawable.julius, 30, 64, 192, 256);

		// Configura a escala, as animacoes e posicao de cada botao
		for (int iIndex = 0; iIndex < vida; iIndex++) {
			vetVidas[iIndex].iPosY = 30;
			vetVidas[iIndex].iPosX = ((iLargura / 6) * 4) + iIndex * 20;
			// vrLeoes.iDirX = (vrRand.nextInt(2) == 0) ? -1 : 1;
			vetVidas[iIndex].fEscalaX = 9;
			vetVidas[iIndex].fEscalaY = 15;
			vetVidas[iIndex].fAlpha = 1.0f;
			vetVidas[iIndex].criaAnimacao(1, false, vrQuadroVidas);
		}

		vetLeoes = new ArrayList<CSprite>();

		// Cria o objeto temporizador para criacao dos Leos
		vrTempoCriacaoLeos = new CIntervaloTempo();
		vrTempoCriacaoLeos.reiniciaTempo(1000);

		// Cria o objeto randomizador
		vrRand = new Random();

		CQuadro[] vrQuadrosNumero0 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero0[iIndex] = new CQuadro(0);
		}
		CQuadro[] vrQuadrosNumero1 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero1[iIndex] = new CQuadro(1);
		}
		CQuadro[] vrQuadrosNumero2 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero2[iIndex] = new CQuadro(2);
		}
		CQuadro[] vrQuadrosNumero3 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero3[iIndex] = new CQuadro(3);
		}
		CQuadro[] vrQuadrosNumero4 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero4[iIndex] = new CQuadro(4);
		}
		CQuadro[] vrQuadrosNumero5 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero5[iIndex] = new CQuadro(5);
		}
		CQuadro[] vrQuadrosNumero6 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero6[iIndex] = new CQuadro(6);
		}
		CQuadro[] vrQuadrosNumero7 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero7[iIndex] = new CQuadro(7);
		}
		CQuadro[] vrQuadrosNumero8 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero8[iIndex] = new CQuadro(8);
		}
		CQuadro[] vrQuadrosNumero9 = new CQuadro[10];
		for (int iIndex = 0; iIndex < 10; iIndex++) {
			vrQuadrosNumero9[iIndex] = new CQuadro(9);
		}

		vrSpriteNumeros = new CSprite(vrOpenGL, R.drawable.numbers, 100, 100,
				1000, 300);
		vrSpriteNumeros.iPosX = 160;
		vrSpriteNumeros.iPosY = 30;
		vrSpriteNumeros.fEscalaX = 20;
		vrSpriteNumeros.fEscalaY = 20;
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero0);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero1);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero2);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero3);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero4);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero5);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero6);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero7);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero8);
		vrSpriteNumeros.criaAnimacao(1, false, vrQuadrosNumero9);
		vrSpriteNumeros.configuraAnimcaceoAtual(0);

		vrSpritePontuacao = new CSprite(vrOpenGL, R.drawable.numbers, 100, 100,
				1000, 300);
		vrSpritePontuacao.iPosX = 120;
		vrSpritePontuacao.iPosY = 30;
		vrSpritePontuacao.fEscalaX = 20;
		vrSpritePontuacao.fEscalaY = 20;
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero0);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero1);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero2);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero3);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero4);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero5);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero6);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero7);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero8);
		vrSpritePontuacao.criaAnimacao(1, false, vrQuadrosNumero9);
		vrSpritePontuacao.configuraAnimcaceoAtual(0);

		vrSpriteTelaFinalGamoOver = new CSprite(vrOpenGL, R.drawable.gameover,
				iLargura, iAltura, iLargura, iAltura);
		vrSpriteTelaFinalGamoOver.iPosX = iLargura / 2;
		vrSpriteTelaFinalGamoOver.iPosY = iAltura / 2;
		vrSpriteTelaFinalGamoOver.fEscalaX = iLargura / 2;
		vrSpriteTelaFinalGamoOver.fEscalaY = iAltura / 2;

		vrSpriteTelaFinal = new CSprite(vrOpenGL, R.drawable.vitoria, 480, 350,
				480, 350);
		vrSpriteTelaFinal.iPosX = iLargura / 2;
		vrSpriteTelaFinal.iPosY = iAltura / 2;
		vrSpriteTelaFinal.fEscalaX = 240;
		vrSpriteTelaFinal.fEscalaY = 125;

		btnPausa = new CSprite(vrOpenGL, R.drawable.sair, 128, 42, 128, 128);
		btnPausa.iPosX = iLargura / 2;
		btnPausa.iPosY = 20;
		btnPausa.fEscalaX = 64;
		btnPausa.fEscalaY = 21;

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

	private void jogo(GL10 vrOpenGL) {
		if (CGerenteEventos.vrEventosTouch.telaClicada() == true) {
			trataClique();
		}
		if (!finalizaJogo) {
			if (!pausaJogo) {
				// if (btnPausa.colidePonto(
				// (int) CGerenteEventos.vrEventosTouch.fPosX, iAltura
				// - ((int) CGerenteEventos.vrEventosTouch.fPosY))) {
				// pausaJogo = false;
				// }
				atualiLeoes(vrOpenGL);
				atualizatelaJogo();
				atualizaPersonagem();
				desenhaLeoes();
				atualizaPontuacao();
				atualizaVidas();
				atualizaChicotada();
			} else {
				atualizaPausa();
			}
		} else {
			terminaJogo();
		}
	}

	private void atualizaPausa() {
		vrSpriteTituloPausa.desenhaSprite();
		vrSpriteTituloPausa.atualizaSprite();
	}

	private void trataClique() {
		if (btnPausa.colidePonto(
				(int) CGerenteEventos.vrEventosTouch.fPosX - 10, iAltura
						- ((int) CGerenteEventos.vrEventosTouch.fPosY))) {
			pausaJogo = true;
		} else {
			if (finalizaJogo == true) {
				finalizaJogo = false;
				menu();
				zeraVariaveis();
				iniciaJogo();
			}
			pausaJogo = false;

			chicotada();
		}
	}

	private void terminaJogo() {
		if (vida == 0) {
			vrSpriteTelaFinalGamoOver.atualizaSprite();
			vrSpriteTelaFinalGamoOver.desenhaSprite();
			
		} else {
			vrSpriteTelaFinal.atualizaSprite();
			vrSpriteTelaFinal.desenhaSprite();
		}
	}

	private void atualizaVidas() {
		for (int i = 0; i < 5; i++) {
			vetVidas[i].atualizaSprite();
			vetVidas[i].desenhaSprite();
		}
	}

	private void atualizaPontuacao() {
		if ((unidade % 10 == 0) && (unidade > 0)) {
			unidade = 0;
			dezena++;
			tempoCriacaoLeoes = tempoCriacaoLeoes - 300;
			temppoDePausa -= 10;
		}
		vrSpriteNumeros.configuraAnimcaceoAtual(unidade);
		vrSpritePontuacao.configuraAnimcaceoAtual(dezena);
		vrSpriteNumeros.atualizaSprite();
		vrSpriteNumeros.desenhaSprite();
		vrSpritePontuacao.atualizaSprite();
		vrSpritePontuacao.desenhaSprite();
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
			vrLeoes.iPosX = iLargura - 100;
			vrLeoes.iDirX = 6;
			vrLeoes.fAngulo = -15;
			break;
		case 1:
			vrLeoes.iPosX = 100;
			vrLeoes.iDirX = -6;
			vrLeoes.fAngulo = 15;
			break;
		case 2:
			vrLeoes.iPosX = 255;
			vrLeoes.iDirX = -3;
			vrLeoes.fAngulo = 2;
			break;
		case 3:
			vrLeoes.iPosX = 400;
			vrLeoes.iDirX = 0;
			break;
		case 4:
			vrLeoes.iPosX = 545;
			vrLeoes.iDirX = 3;
			vrLeoes.fAngulo = -2;
			break;
		}
		// vrLeoes.iPosX = vrRand.nextInt(iLargura);

		vrLeoes.iPosY = iAltura - 180;
		vrLeoes.iDirY = -1;
		// vrLeoes.iDirX = (vrRand.nextInt(2) == 0) ? -1 : 1;
		vrLeoes.fEscalaX = 18;
		vrLeoes.fEscalaY = 29;

		vrLeoes.fAlpha = 0.0f;
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
			vrTempoCriacaoLeos.reiniciaTempo(tempoCriacaoLeoes);
		}

		// Atualiza a pos dos Leoes e remove os leoes que ja sairam da
		// tela
		for (int iIndex = vetLeoes.size() - 1; iIndex >= 0; iIndex--) {

			if (vetLeoes.get(iIndex).iPosY < 180) {
				vetLeoes.get(iIndex).estado = false;
				vetLeoes.get(iIndex).fAlpha = 0.0f;
				vetLeoes.get(iIndex).mordeu = true;
				mordida();
			}
			if (vetLeoes.get(iIndex).estado) {
				vetLeoes.get(iIndex).iPosY -= 10;
				vetLeoes.get(iIndex).iPosX -= vetLeoes.get(iIndex).iDirX * 2;
				vetLeoes.get(iIndex).fAlpha += 0.5;
			} else {
				vetLeoes.get(iIndex).iPosY += 10;
				vetLeoes.get(iIndex).iPosX += vetLeoes.get(iIndex).iDirX * 2;
				vetLeoes.get(iIndex).configuraAnimcaceoAtual(1);

			}
			if (vetLeoes.get(iIndex).mordeu == true) {
				if (vetLeoes.get(iIndex).iPosY >= iAltura - 230) {
					vetLeoes.get(iIndex).fAlpha = 0.9f;
					vetLeoes.get(iIndex).iPosX = iLargura / 2;
					vetLeoes.get(iIndex).iPosY = 182;
					vetLeoes.get(iIndex).mordeu = false;
				}
			}
			if (vetLeoes.get(iIndex).iPosY >= iAltura - 200) {
				vetLeoes.get(iIndex).fAlpha -= 0.5;
			}
			if (iIndex <= 1) {
				if ((vetLeoes.get(iIndex).iPosY >= iAltura - 195)
						&& (vetLeoes.get(iIndex).estado == false)) {
					vetLeoes.remove(iIndex);
				}
			} else {
				if ((vetLeoes.get(iIndex).iPosY >= iAltura - 180)
						&& (vetLeoes.get(iIndex).estado == false)) {
					vetLeoes.remove(iIndex);
				}
			}
		}

	}

	private void mordida() {
		vrSpritechicotada.iPosX = iLargura / 2;
		vrSpritechicotada.iPosY = 150;
		vrSpritechicotada.fEscalaX = 60;
		vrSpritechicotada.fEscalaY = 60;
		vrSpritechicotada.configuraAnimcaceoAtual(0);
		if (vrSpritechicotada.retornaAnimacaoAtual().animacaoFinalizada()) {
			vrSpritechicotada.reiniciaAnimacao();
		}
		vida--;
		CGerenteSons.vrEfeitos.reproduzSom(vetCodigoSons[1]);
		if (vida != 0) {
			vetVidas[vida].fAlpha = 0.3f;
		} else {
			CGerenteSons.vrEfeitos.reproduzSom(vetCodigoSons[2]);
			CGerenteSons.vrMusica.pausaMusica();
			finalizaJogo = true;
		}
	}

	// Metodo utilizado para realizar uma pausa no loop da aplicacao
	private void pausa() {
		try {
			Thread.sleep(temppoDePausa);
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
				// CGerenteSons.vrMusica.carregaMusica("menu.mid", true);
				// CGerenteSons.vrMusica.reproduzMusica();

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
							CGerenteSons.vrEfeitos
									.reproduzSom(vetCodigoSons[1]);
							CGerenteSons.vrMusica.carregaMusica("musica.mid",
									true);
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
		vrSpriteTituloAjuda.atualizaSprite();
		vrSpriteTituloAjuda.desenhaSprite();
		if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_DOWN) {
			iEstado = MENU;
		}
	}

	private void JuliusAnda(GL10 vrOpenGL) {
		if (iJuliusEstado == 0) {
			vrSpriteJulius.configuraAnimcaceoAtual(0);
			vrSpriteJulius.iPosX += 5;
			vrSpriteJulius.iEspelhamento = 1;

			if (vrSpriteJulius.iPosX >= iLargura / 2) {
				iJuliusEstado = 1;
				vrSpriteJulius.configuraAnimcaceoAtual(1);
				temppoDePausa = 100;
				iniciaJogo();
			}
			atualizatelaJogo();
			atualizaPersonagem();
		} else {
			iJuliusEstado = 1;
			jogo(vrOpenGL);
		}

	}

	private void chicotada() {
		vrSpritechicotada.fEscalaX = 24;
		vrSpritechicotada.fEscalaY = 24;
		CGerenteSons.vrEfeitos.reproduzSom(1);
		vrSpritechicotada.iPosX = (int) CGerenteEventos.vrEventosTouch.fPosX;
		vrSpritechicotada.iPosY = iAltura
				- ((int) CGerenteEventos.vrEventosTouch.fPosY);
		vrSpritechicotada.configuraAnimcaceoAtual(1);
		if (vrSpritechicotada.retornaAnimacaoAtual().animacaoFinalizada()) {
			vrSpritechicotada.reiniciaAnimacao();
		}
		for (int i = 0; i < vetLeoes.size(); i++) {
			if (vetLeoes.get(i).colidePonto(
					(int) CGerenteEventos.vrEventosTouch.fPosX - 10,
					iAltura - ((int) CGerenteEventos.vrEventosTouch.fPosY))
					&& (vetLeoes.get(i).estado == true)) {
				vetLeoes.get(i).estado = false;
				vetLeoes.get(i).fAlpha = 0.5f;
				acertouLeao();
			}
		}
	}

	private void acertouLeao() {
		leaoMorto++;
		unidade++;
		if (dezena > 9) {
			finalizaJogo = true;
		}
	}

	private void atualizaPersonagem() {
		vrSpriteJulius.atualizaSprite();
		vrSpriteJulius.desenhaSprite();
	}

	private void atualizaChicotada() {
		if (!vrSpritechicotada.retornaAnimacaoAtual().animacaoFinalizada()) {
			vrSpritechicotada.atualizaSprite();
			vrSpritechicotada.desenhaSprite();
		}
	}

	private void atualizatelaJogo() {
		vrSpriteFundoJogo.atualizaSprite();
		vrSpriteFundoJogo.desenhaSprite();
		btnPausa.atualizaSprite();
		btnPausa.desenhaSprite();
	}

	public void iniciaJogo() {
		vida = 5;
		for (int i = 0; i < 5; i++) {
			vetVidas[i].fAlpha = 1;
		}
//		zeraVariaveis();
	}

	public void zeraVariaveis() {
		unidade = 0;
		dezena = 0;
		leaoMorto=0;
		centena = 0;
		tempoCriacaoLeoes = 3000;
		temppoDePausa = 100;
		iJuliusEstado=0;
		vrSpriteJulius.iPosX=0;
		vetLeoes.removeAll(vetLeoes)	;	
	}
}