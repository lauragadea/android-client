package com.example.root.android_client;

import java.util.ArrayList;
import java.util.List;


public class Response {
    private List<Producto> productos = new ArrayList<>();

    public List<Producto> getProductos(){
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
