package com.example.root.android_client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class LoadMovementJSONTask extends AsyncTask<String, Void, RespuestaMovimiento> {

    private static final String TAG = MainActivity.class.getSimpleName();

    public LoadMovementJSONTask(LoadMovementJSONTask.Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(List<Movimiento> androidList);

        void onError();
    }

    private LoadMovementJSONTask.Listener mListener;


    @Override
    protected RespuestaMovimiento doInBackground(String... strings) {

        try {
            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();
            Log.d(TAG, stringResponse);
            //Response res = gson.fromJson(stringResponse, Response.class);
            RespuestaMovimiento res = new RespuestaMovimiento();
            Movimiento [] movimientos= gson.fromJson(stringResponse, Movimiento[].class);
            List<Movimiento> movimientoList = Arrays.asList(movimientos);
            res.setMovimientos(movimientoList);
            //return gson.fromJson(stringResponse, Response.class);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(RespuestaMovimiento response) {
        Log.d(TAG, "on post exec");
        if (response != null) {

            mListener.onLoaded(response.getMovimientos());

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String myurl) throws IOException {
        Log.d(TAG, "LoadJSON()");
        //InputStream is = null;
        // esta es la url que se tiene en cuenta
        URL url = new URL("http://192.168.1.131:8000/movimientos");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            return response.toString();


        } finally {
            urlConnection.disconnect();
        }

    }

}
