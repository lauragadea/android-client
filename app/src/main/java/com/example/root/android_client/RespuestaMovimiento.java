package com.example.root.android_client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 27/01/17.
 */

public class RespuestaMovimiento {
    private List<Movimiento> movimientos = new ArrayList<>();

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

}
