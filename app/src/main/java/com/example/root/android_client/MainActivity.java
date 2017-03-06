package com.example.root.android_client;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView mMainListView;

    String[] values = new String[]{"Menu de Comidas",
            "Mi dinero",
            "Ultimos movimientos",
            "Salir"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mMainListView = (ListView) findViewById(R.id.main_list_view);


        //Defino adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        //Asigno ela dapter al mMainListView
        mMainListView.setAdapter(adapter);

        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ListView clicked item index
                int itemPosition = position;

                //ListView clicked item value
                String itemValue = (String) mMainListView.getItemAtPosition(position);

                switch (itemPosition) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //ojo con esta linea ->
                        MainActivity.super.onResume();
                        Intent intent2 = new Intent(MainActivity.this, MyAccountActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(MainActivity.this, MovementActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        setContentView(R.layout.activity_login);
                        break;
                }
            }

            ;

        });
    }





}
