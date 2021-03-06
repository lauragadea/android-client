package com.example.root.android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class MyAccountActivity extends AppCompatActivity implements LoadAccountJSONTask.Listener {

    public static final String URL = "192.168.1.131:8000/saldo";

    private TextView mSaldo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mSaldo = (TextView) findViewById(R.id.saldo);

        new LoadAccountJSONTask(this).execute(URL);

        Button btnGo = (Button) findViewById(R.id.btnGo);

        btnGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                EditText montoAgregado = (EditText) findViewById(R.id.monto);
                Intent intent = new Intent(MyAccountActivity.this, CargarSaldo.class);
                intent.putExtra("montoAgregado", montoAgregado.getText().toString());
                intent.putExtra("saldoViejo", mSaldo.getText().toString());
                startActivity(intent);


            }
        });




    }



    public void onLoaded(Saldo saldo){
        Log.d("tag", "estamos en OnLoaded y el saldo es " + saldo.getValor());
        mSaldo.setText(getApplicationContext().getResources().getString(R.string.saldo_data) + String.valueOf(saldo.getValor()));
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

 }
