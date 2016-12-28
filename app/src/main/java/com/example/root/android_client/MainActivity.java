package com.example.root.android_client;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    public static final String URL = "192.168.1.131:8000/productos";

    private List<HashMap<String, String>> mProductosMapList = new ArrayList<>();

    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_PRECIO = "precio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ojo, debe ir a activity_main, y esa me redirige a start_layout
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
        //mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.activity_main, new LoadJSONTask(this).execute(URL)));

    }

    public void onLoaded(List<Producto> productoList){
        for (Producto producto : productoList) {
            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_DESCRIPCION, producto.getDescripcion());
            map.put(KEY_PRECIO, producto.getPrecio());

            mProductosMapList.add(map);
        }
        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this, mProductosMapList.get(i).get(KEY_DESCRIPCION),Toast.LENGTH_LONG).show();
    }
    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, mProductosMapList, R.layout.list_item,
                new String[] { KEY_DESCRIPCION, KEY_PRECIO },
                new int[] { R.id.descripcion,R.id.precio });

        mListView.setAdapter(adapter);

    }
/*
    public void start(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        //setContentView(R.layout.activity_listview);

    }
*/
public void sendMessage(View view) {
    Toast.makeText(this, "Compra realizada con exito", Toast.LENGTH_SHORT).show();
}



}


