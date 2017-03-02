package com.example.root.android_client;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/01/17.
 */

public class MovementAdapter extends ArrayAdapter<Movimiento> {

    private Activity activity;
    private List<Movimiento> lMovimiento;
    private static LayoutInflater inflater = null;

    public MovementAdapter (Activity activity, int textViewResourceId, List<Movimiento> _lMovimiento) {
        super(activity, textViewResourceId, _lMovimiento);
        try {
            this.activity = activity;
            this.lMovimiento = _lMovimiento;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lMovimiento.size();
    }


    public static class ViewHolder {
        public TextView descripcion;
        public TextView monto;
        public TextView fecha;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.movement_list_item, null);
                holder = new ViewHolder();

                holder.descripcion = (TextView) vi.findViewById(R.id.descripcion);
                holder.monto = (TextView) vi.findViewById(R.id.monto);
                holder.fecha = (TextView) vi.findViewById(R.id.fecha);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.descripcion.setText(lMovimiento.get(position).getDescripcion());
            holder.monto.setText(lMovimiento.get(position).getMonto());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = format.format(lMovimiento.get(position).getFecha());
            holder.fecha.setText(fecha);



        } catch (Exception e) {


        }
        return vi;
    }
}
