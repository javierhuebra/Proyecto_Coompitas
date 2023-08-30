package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre del producto no puede ser nulo")
    @Size(min = 3, max = 120, message = "El nombre del producto debe tener entre 3 y 120 caracteres")
    private String nombre;
    @NotBlank(message = "La descripción del producto no puede ser nula")
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    @NotBlank(message = "La imagen del producto no puede ser nula")
    @Column(columnDefinition = "TEXT")
    private String imagen;
    @NotBlank(message = "El precio del producto no puede ser nulo")
    @Min(value = 0, message = "El precio del producto no puede ser menor a 0")
    private double precio;
    @NotBlank(message = "El stock del producto no puede ser nulo")
    @Min(value = 0, message = "El stock del producto no puede ser menor a 0")
    private int stock;
    private boolean disponible;

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


    public Producto() {
    }

    public Producto(String nombre, String descripcion, String imagen, int precio, int stock, boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.stock = stock;
        this.disponible = disponible;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
