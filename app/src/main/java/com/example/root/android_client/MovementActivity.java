package com.example.root.android_client;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MovementActivity  extends AppCompatActivity implements LoadMovementJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListViewMov;

    public static final String URL = "192.168.1.131:8000/movimientos";

    //private List<HashMap<String, String>> mMovimientosMapList = new ArrayList<>();
    private List<Movimiento> mMovimientosList = new ArrayList<>();

    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_MONTO = "monto";
    private static final String KEY_FECHA = "fecha";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);

        mListViewMov = (ListView) findViewById(R.id.list_mov_view);

        new LoadMovementJSONTask(this).execute(URL);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



       // Toast.makeText(this, mMovimientosList.get(i).get(KEY_DESCRIPCION),Toast.LENGTH_LONG).show();
    }


    public void onLoaded(List<Movimiento> movimientoList){

        mMovimientosList.addAll(movimientoList);
        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }


    private void loadListView() {

        MovementAdapter movAdap;
        ArrayList<Movimiento> myListItems  = new ArrayList<>();


        movAdap= new MovementAdapter (MovementActivity.this, 0, mMovimientosList);

        mListViewMov.setAdapter(movAdap);
    }

}
