package com.proyecto.coompitas.classes;

import com.proyecto.coompitas.models.Producto;

public class ProdCant {
    private int cantidad;
    private Producto producto;

    public ProdCant() {
    }

    public ProdCant(int cantidad, Producto producto){
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
