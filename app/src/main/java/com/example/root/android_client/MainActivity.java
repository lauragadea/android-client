package com.example.root.android_client;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView mMainListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mMainListView = (ListView) findViewById(R.id.main_list_view);

        String[] values = new String[]{"Mi dinero",
                "Menu de Comidas",
                "Ultimas compras",
                "Salir"
        };

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

                //show alert
                Toast.makeText(getApplicationContext(), "Posicion: "+itemPosition+" ListItem: "+itemValue, Toast.LENGTH_LONG).show();
            }
        });

    }



    public void start(View view){

        Intent intent = new Intent(this, ListFoodActivity.class);
        startActivity(intent);


    }



}
