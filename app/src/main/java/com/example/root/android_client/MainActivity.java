package com.example.root.android_client;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ojo, debe ir a activity_main, y esa me redirige a start_layout
        setContentView(R.layout.start_layout);

       /* ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

*/
        new DownloadTask().execute("http://www.google.com/");
    }

    public void start(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        //setContentView(R.layout.activity_listview);

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                Log.e("tag", "ocurrio un error:", e);
                Log.e("tag", Log.getStackTraceString(e));
                return "Unable to retrieve data. URL may be invalid";
            }
        }

        @Override
        protected void onPostExecute(String resp) {

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

            Toast.makeText(MainActivity.this, "ejecutado", Toast.LENGTH_LONG).show();
        }

    }

    private String downloadContent(String myurl) throws IOException {
        //InputStream is = null;
       // int length = 500;
        URL url = new URL("http://192.168.1.131:8000/usuario");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String resp = readStream(in);

            Log.d(TAG, resp);
            
            return resp;
        //esta linea para post
        //conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");



        } finally {
            urlConnection.disconnect();
        }

    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {

                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}


