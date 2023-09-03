package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "cantidades_descuentos")
public class CantDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad de los campos debe ser distinta de 0")
    private int cantidad;
    @NotNull(message = "El descuento no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El descuento de los campos debe ser distinto de 0")
    private double descuentoAplicado;

    //PRODUCTO QUE POSEE LA CANTIDAD Y DESCUENTO (Producto)
    //Relación 1 : N con CantDesc (Un producto puede tener muchas cantidades y descuentos, una cantidad y descuento pertenece a un solo producto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;




    public CantDesc() {
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
}
