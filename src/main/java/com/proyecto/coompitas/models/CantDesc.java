package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "cantidades_descuentos")
public class CantDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser menor a 0")
    private int cantidad;
    @NotBlank(message = "El descuento no puede ser nulo")
    @Min(value = 0, message = "El descuento no puede ser menor a 0")
    private double descuentoAplicado;

    @Column(updatable = false)
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }


    public CantDesc() {
    }

    public CantDesc(int cantidad, double descuento) {
        this.cantidad = cantidad;
        this.descuentoAplicado = descuento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getDescuento() {
        return descuentoAplicado;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }
}
