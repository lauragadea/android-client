package com.example.root.android_client;

/**
 * Created by root on 13/12/16.
 */

public class Producto {
    private int id;
    private String descripcion;
    private String precio;
    private boolean checked;

    public String getDescripcion(){
        return descripcion;
    }

    public String getPrecio(){
        return precio;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

}
