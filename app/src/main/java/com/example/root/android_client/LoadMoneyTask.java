package com.example.root.android_client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadMoneyTask extends AsyncTask<Integer, Void, Saldo> {

    @Override
    protected Saldo doInBackground(Integer... pParams) {
        //do your request in here so that you don't interrupt the UI thread
        int saldoNuevo;
        saldoNuevo = pParams[0];
        Log.d("tag", "nuevo saldo en do in back: " + saldoNuevo);
        try {
            String stringResponse = postMoney(saldoNuevo);


            //YA VEO QUE HACER
            //Saldo sal = gson.fromJson(stringResponse, Saldo.class);
            Saldo sal = new Saldo();
            sal.setValor(350);
            return sal;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    private String postMoney(int saldo) throws IOException {

        // esta es la url que se tiene en cuenta
        URL url = new URL("http://192.168.1.131:8000/saldo/" + saldo);
        Log.d("tag", "URL FINAL -> " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            Log.d("tag", "response.tostring() = "+ response.toString());
            return response.toString();

        } catch (MalformedURLException e) {
                e.printStackTrace();
                return "error";
        } catch (IOException e) {
                e.printStackTrace();
                return "error";
        } finally {
            urlConnection.disconnect();
        }

    }
}
