package com.example.root.android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {
    public static final String URL = "192.168.1.131:8000/pago";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //supuesto total
        int total = 50;

        new LoadPaymentTask().execute(total);

        Button continuar = (Button) findViewById(R.id.continuar);

        continuar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent5 = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent5);


            }
        });


    }




}
