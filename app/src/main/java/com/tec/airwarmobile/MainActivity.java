package com.tec.airwarmobile;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    
    private TextView etiqueta_nombre;
    private TextView etiqueta_puntos;
    private TextView etiqueta_vidas;
    private TextView etiqueta_hp;
    private Button boton_disparo;

    private SensorManager sensor_manager;
    private Jugador jugador;
    private boolean esta_disparando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        etiqueta_nombre = (TextView)findViewById(R.id.name_text);
        etiqueta_hp = (TextView)findViewById(R.id.hp_text);
        etiqueta_puntos = (TextView)findViewById(R.id.points_text);
        etiqueta_vidas = (TextView)findViewById(R.id.lives_text);
        boton_disparo = (Button)findViewById(R.id.button);

        etiqueta_nombre.setTextColor(Color.GREEN);
        boton_disparo.setBackgroundColor(Color.WHITE);
        boton_disparo.setTextColor(Color.RED);

        esta_disparando = false;

        conectar_con_servidor();
        esperar(2000);
        actualizar_stats();
        disparar();
    }

    /**
     * Crea la conexion con el servidor.
     */
    private void conectar_con_servidor() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                jugador = new Jugador();
                jugador.start();
            }
        });
    }

    private void disparar() {

        boton_disparo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    esta_disparando = true;
                }else {
                    esta_disparando = false;
                }
                return true;
            }
        });
    }

    public void actualizar_stats(){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etiqueta_hp.setText("HP: " + jugador.hp);
                        etiqueta_puntos.setText("Puntaje: " + jugador.puntaje);
                        etiqueta_vidas.setText("Vidas: " + jugador.vidas);
                    }
                });
            }
        }, 0 , 500);
    }

    public void onResume(){
        super.onResume();
        sensor_manager.registerListener(this, sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onStop(){
        sensor_manager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
            return;
        }

        try {
            jugador.enviar(JSONHandler.establecer_json_coordenadas(event.values[0], event.values[1], esta_disparando));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void esperar(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
