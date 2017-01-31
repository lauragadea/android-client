package com.example.root.android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListFoodActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    public static final String URL = "192.168.1.46:8000/productos";

    private List<HashMap<String, String>> mProductosMapList = new ArrayList<>();

    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_PRECIO = "precio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);


        new LoadJSONTask(this).execute(URL);
    }

    public void onLoaded(List<Producto> productoList){
        Log.d("tag", "on loaded");
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

    //not working
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        CheckedTextView ctv = (CheckedTextView)view;

        if(ctv.isChecked()){
            Toast.makeText(this, "now it is unchecked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "now it is checked", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(this, mProductosMapList.get(i).get(KEY_DESCRIPCION),Toast.LENGTH_LONG).show();
    }
    private void loadListView() {
        Log.d("tag", "on loadlistview");
        ListAdapter adapter = new SimpleAdapter(ListFoodActivity.this, mProductosMapList, R.layout.list_item,
                new String[] { KEY_DESCRIPCION, KEY_PRECIO },
                new int[] { R.id.descripcion,R.id.precio });

        mListView.setAdapter(adapter);

    }

    public void confirmTransaction(View view) {

        Intent intent = new Intent(this, ConfirmarPedido.class);
        startActivity(intent);
        //Toast.makeText(this, "Compra realizada con exito", Toast.LENGTH_SHORT).show();
    }


}

