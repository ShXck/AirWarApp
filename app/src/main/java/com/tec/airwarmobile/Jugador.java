package com.tec.airwarmobile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Jugador extends Thread{

    private BufferedReader in;
    private PrintWriter out;

    int vidas;
    int hp;
    int puntaje;
    boolean esta_conectado = false;

    public void crear_conexion(){

        try {
            Socket socket = new Socket("192.168.1.6", 8000);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            esta_conectado = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        crear_conexion();

        while (true){

            String jugador_stats = leer();

            try {
                JSONObject stats = JSONHandler.obtener_stats(jugador_stats);

                puntaje = stats.getInt("puntaje");
                hp = stats.getInt("hp");
                vidas = stats.getInt("vidas");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String leer(){

        String resultado = "";
        try {
            resultado = in.readLine();
        }catch (IOException s){
            s.printStackTrace();
        }
        return resultado;
    }

    public void enviar(String data){
        out.println(data);
        out.flush();
    }

}
