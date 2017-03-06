package com.example.root.android_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadPaymentTask extends AsyncTask<Integer, Void, String> {



    @Override
    protected String doInBackground(Integer... pParams) {
        //do your request in here so that you don't interrupt the UI thread
        int total;
        total = pParams[0];
        Log.d("tag", "total a pagar en do in back: " + total);
        try {
            String stringResponse = postPayment(total);

            return "compra realizada";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    private String postPayment(int total) throws IOException {

        // esta es la url que se tiene en cuenta
        URL url = new URL("http://192.168.1.131:8000/pago/" + total);


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
