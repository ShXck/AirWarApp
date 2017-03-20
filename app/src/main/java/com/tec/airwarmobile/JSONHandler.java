package com.tec.airwarmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHandler {

    public static String establecer_json_coordenadas(float x, float y, boolean esta_disparando) throws JSONException {

        JSONObject json_coordenadas = new JSONObject();

        json_coordenadas.put("x",x);
        json_coordenadas.put("y",y);

        if (esta_disparando){
            json_coordenadas.put("instruccion", "disparar");
        }else {
            json_coordenadas.put("instruccion", "none");
        }

        return json_coordenadas.toString();

    }

    public static JSONObject obtener_stats(String string_stats) throws JSONException{

        JSONObject json_stats = new JSONObject(string_stats);

        return json_stats;
    }

}
