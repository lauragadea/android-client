package com.example.root.android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CargarSaldo extends AppCompatActivity {

    public static final String URL = "192.168.1.46:8000/saldo";
    private TextView mSaldoNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_saldo);

        mSaldoNuevo = (TextView) findViewById(R.id.nuevo_saldo);

        int nuevoSaldo = 0;

        String s = getIntent().getStringExtra("tarjeta");
        String mt = getIntent().getStringExtra("montoAgregado");
        String salVie = getIntent().getStringExtra("saldoViejo");
        nuevoSaldo = calcularSaldoNuevo(mt, salVie);
        Log.d("tag", "nuevo saldo es " + nuevoSaldo);

        new LoadMoneyTask().execute(nuevoSaldo);

        mSaldoNuevo.setText("$ " + String.valueOf(nuevoSaldo));

        Button mainMenu = (Button) findViewById(R.id.main_menu);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargarSaldo.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private int calcularSaldoNuevo(String monto, String saldoViejo){
        //Sacar el signo pesos
        String[] separated = monto.split("\\$");
        monto = separated[1].trim();
        int montoAgregado = 0;
        int saldoViejoEnt = 0;
        //convierto mt (string) en int
        try {
            montoAgregado = Integer.parseInt(monto.toString());

        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        //saco signo pesos al saldo viejo

        String[] separated2 = saldoViejo.split("\\$");
        saldoViejo = separated2[1].trim();

        //convierto a saldo viejo en int
        try {
            saldoViejoEnt = Integer.parseInt(saldoViejo.toString());
            Log.d("tag", "saldo viejo =" + saldoViejoEnt);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        return saldoViejoEnt + montoAgregado;

    }
}
