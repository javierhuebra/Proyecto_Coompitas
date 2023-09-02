package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotBlank(message = "La url de la imagen no puede ser nula")
    @Column(columnDefinition = "TEXT")//Quizas la url sea larga por eso queda text(?
    private String imagen;
    @NotNull(message = "El precio del producto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "No ha seteado un precio para este producto")
    private double precio;
    @NotNull(message = "El stock del producto no puede ser nulo")
    @Min(value = 0, message = "El stock del producto no puede ser menor a 0")
    private int stock;
    private boolean disponible = true;

    @NotBlank(message = "Debe seleccionar una categoría")
    private String categoria;

    //USUARIO QUE POSEE EL PRODUCTO (Proveedor)
    //Relación 1 : N con Producto (Un usuario proveedor puede proveer muchos productos, un producto tiene un solo proveedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private User proveedor;

    
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;



    public Producto() {
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getProveedor() {
        return proveedor;
    }

    public void setProveedor(User proveedor) {
        this.proveedor = proveedor;
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

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }

}
