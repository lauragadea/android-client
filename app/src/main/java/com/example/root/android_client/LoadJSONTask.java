package com.example.root.android_client;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.String.*;



/**
 * Created by root on 13/12/16.
 */

public class LoadJSONTask extends AsyncTask<String, Void, Response> {

    private static final String TAG = MainActivity.class.getSimpleName();

        public LoadJSONTask(Listener listener) {

            mListener = listener;
        }

        public interface Listener {

            void onLoaded(List<Producto> androidList);

            void onError();
        }

        private Listener mListener;

        @Override
        protected Response doInBackground(String... strings) {
            //do your request in here so that you don't interrupt the UI thread
            Log.d(TAG, "a ver stringRespnonse?");
            try {
                String stringResponse = loadJSON(strings[0]);
                Gson gson = new Gson();
                Log.d(TAG, stringResponse);
                //Response res = gson.fromJson(stringResponse, Response.class);
                Response res = new Response();
                Producto [] productos = gson.fromJson(stringResponse, Producto[].class);
                List<Producto> productList = Arrays.asList(productos);
                res.setProductos(productList);
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
    protected void onPostExecute(Response response) {
        Log.d(TAG, "on post exec");
        if (response != null) {

            mListener.onLoaded(response.getProductos());

        } else {

            mListener.onError();
        }
        //TextView tv = (TextView) findViewById(R.id.tv);
        //if(resp !=null){
        //   try{
        //       String test;
        //Log.d(TAG, resp);
//                    JSONObject jsonRootObject= (JSONObject) new JSONTokener(resp).nextValue();
        //JSONObject json2 = jsonRootObject.getJSONObject("response");
        //Log.d(TAG, json2.toString());
        //json2.get("sihpments");
        //test = "lala";
        // test =  json2.get("shipments").toString();
        //Log.d(TAG, test);
        //       Log.d(TAG, "recien paso el test");

        //JSONObject response = jsonRootObject.getJSONObject("response");


        // er = response.getString("external_reference");

        //tv.setText(tv.getText()+ "\t status' " + status);

        // }catch(JSONException e){
        //     e.printStackTrace();
        // }

        //Toast.makeText(MainActivity.this, "ejecutado", Toast.LENGTH_LONG).show();
    }

    private String loadJSON(String myurl) throws IOException {
        Log.d(TAG, "LoadJSON()");
        //InputStream is = null;
        // esta es la url que se tiene en cuenta
        URL url = new URL("http://192.168.1.131:8000/productos");
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


        /*
            urlConnection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String resp = readStream(in);

            Log.d(TAG, resp);

            return resp;
        */

            //esta linea para post
            //conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");



        } finally {
            urlConnection.disconnect();
        }

    }
}
