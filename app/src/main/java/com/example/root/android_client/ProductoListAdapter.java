package com.example.root.android_client;

import android.widget.ArrayAdapter;

/**
 * Created by root on 17/03/17.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


public class ProductoListAdapter extends ArrayAdapter<Producto> {
    private OnItemCheckedListener onItemCheckedListener;

    public ProductoListAdapter(Context context, ArrayList<Producto> objects) {
        super(context, R.layout.list_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Producto producto = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView descripcionTextView = (TextView) convertView.findViewById(R.id.descripcion);
        TextView precioTextView = (TextView) convertView.findViewById(R.id.precio);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        descripcionTextView.setText(producto.getDescripcion());
        precioTextView.setText(producto.getPrecio());
        checkBox.setChecked(false); // default value
        checkBox.setTag(producto);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto selectedProduct = (Producto) v.getTag();
                if (null != selectedProduct) {
                    selectedProduct.setChecked(!selectedProduct.isChecked());
                    onItemCheckedListener.onItemChecked(selectedProduct);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public interface OnItemCheckedListener {
        void onItemChecked(Producto product);
    }
}
