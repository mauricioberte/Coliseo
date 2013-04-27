/********************************************
 Programa: Exemplo14
 Descri‹o: apresenta captura e tratamento de eventos touch
 Autor: Silvano Maneck Malfatti
 Local: Unochapeco
 ********************************************/

//Pacote da aplicacao
package gameEngine;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CAcelerometro implements SensorEventListener
{
	//Atributos da classe
	boolean bAcelerometro = false;
	float fForcaX = 0.0f, fForcaY = 0.0f, fForcaZ = 0.0f;
	
	//Metodo utilizado para a inicializacao de recursos
	public void inicializaAcelerometro(Activity vrActivity)
	{
		//Obtem a referencia ao gerente de sensores do Android
		SensorManager vrSensorManager = (SensorManager)vrActivity.getSystemService(Context.SENSOR_SERVICE);
		
		//Verifica se existe pelo menos 1 acelerometro no dispositivo
		bAcelerometro = vrSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0;
		
		//Se o sernsor foi encontrado entao obtem a referencia para o mesmo
		if(bAcelerometro)
		{
			Sensor vrSensor = vrSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			vrSensorManager.registerListener(this, vrSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	//Metodo que realiza o poll no dispositivo para a captura de eventos
	public void onSensorChanged(SensorEvent event) 
	{
		//Armazena os valores do acelerometro
		fForcaX = event.values[1];
		fForcaY = -event.values[0];
		fForcaZ = event.values[2];
	}
	
	//Metodo que captura a alteracao na acuracia
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
	}
	
	//Metodo retorna a forca em X
	public float getAcelX()
	{
		return fForcaX;
	}
	
	//Metodo retorna a forca em Y
	public float getAcelY()
	{
		return fForcaY;
	}
		
	//Metodo retorna a forca em Y
	public float getAcelZ()
	{
		return fForcaZ;
	}
}
