package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Size(min = 1, max = 255, message = "El mensaje debe tener entre 1 y 255 caracteres")
    private String mensaje;

    private int estadoDelPedido;

    private double precioTotal;

    //PRODUCTOS QUE FORMAN EL PEDIDO
    //Relación N : N con Pedido (Un pedido puede tener muchos productos, un producto puede estar en muchos pedidos)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pedidos_productos",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;


    //CÁMARA DONDE ESTA EL PEDIDO
    //Relación N : 1 con Camara (Un pedido tiene una sola cámara, una cámara puede tener muchos pedidos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camara_id")
    private Camara camara;

    //PEDIDOS DEL USUARIO
    //Relación N : 1 con User (Un pedido tiene un solo usuario, un usuario puede tener muchos pedidos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User comprador;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    public Pedido() {
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public User getComprador() {
        return comprador;
    }

    public void setComprador(User comprador) {
        this.comprador = comprador;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Camara getCamara() {
        return camara;
    }

    public void setCamara(Camara camara) {
        this.camara = camara;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getEstadoDelPedido() {
        return estadoDelPedido;
    }

    public void setEstadoDelPedido(int estadoDelPedido) {
        this.estadoDelPedido = estadoDelPedido;
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
