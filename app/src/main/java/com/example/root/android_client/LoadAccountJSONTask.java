package com.example.root.android_client;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.*;

/**
 * Created by root on 05/01/17.
 */

public class LoadAccountJSONTask extends AsyncTask<String, Void, Saldo> {

    public LoadAccountJSONTask(LoadAccountJSONTask.Listener listener) {

        mListener = listener;
    }

    public interface Listener {


        void onLoaded(Saldo saldo);

        void onError();
    }

    private LoadAccountJSONTask.Listener mListener;

    @Override
    protected Saldo doInBackground(String... strings) {
        //do your request in here so that you don't interrupt the UI thread

        try {
            String stringResponse = loadAccountJSON(strings[0]);

            //StringTokenizer tokens = new StringTokenizer(stringResponse, ":");
            //String first = tokens.nextToken();
            //String second = tokens.nextToken();
            //second.split("\\[");
            //Log.d("tag", "second " + second );
            //Gson gson = new Gson();
            Log.d("TAG", stringResponse);
            //Saldo sal = gson.fromJson(stringResponse, Saldo.class);
            Saldo sal = new Saldo();
            sal.setValor(350);
            return sal;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Saldo saldo) {
        if (saldo != null) {
            Log.d("tag", "saldo en onpostexec es: " + saldo);
            mListener.onLoaded(saldo);

        } else {

            mListener.onError();
        }

    }

    private String loadAccountJSON(String myurl) throws IOException {

        // esta es la url que se tiene en cuenta
        URL url = new URL("http://192.168.1.131:8000/saldo");
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



            //esta linea para post
            //conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");



        } finally {
            urlConnection.disconnect();
        }

    }
}
