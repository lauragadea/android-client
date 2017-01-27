package com.example.root.android_client;

import java.util.Date;

/**
 * Created by root on 27/01/17.
 */

public class Movimiento {

    private int id;
    private String descripcion;
    private String monto;
    private Date fecha;

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }
}
