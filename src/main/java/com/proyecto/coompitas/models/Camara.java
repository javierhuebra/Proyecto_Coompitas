package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "camaras")
public class Camara{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@NotBlank(message = "Debe seleccionar una fecha de expiración")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDeExpiracion;
    //@NotBlank(message = "Debe seleccionar un estado para la cámara")
    //@Min(value = 0, message = "El estado de la cámara no puede ser menor a 0")
    private int estadoDeLaCamara;

    private double precioEnvio;

    //Relacion que se usa para asignarle una dirección a la camara, solo se relaciona con direcciones
    //Relación 1 : N con Direccion (Una camara tiene una sola direccion, una direccion puede tener muchas camaras)
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "direccion_id")
    private Direccion direccionEnvio;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    //USUARIO CREADOR DE LA CÁMARA
    //Relación N : 1 con User (Una cámara tiene un solo usuario, un usuario puede tener muchas cámaras)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_user_id")
    private User creador;

    //USUARIO PROVEEDOR DE LA CÁMARA
    //Relación N : 1 con User (Una cámara tiene un solo usuario proveedor, un usuario proveedor puede proveer muchas cámaras)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_user_id")
    private User proveedor;

    //PEDIDOS QUE CONTIENEN LA CÁMARA (Pedido)
    //Relación 1 : N con Pedido (Una camara tiene muchos pedidos, un pedido tiene una sola camara)
    @OneToMany(mappedBy = "camara", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    //USUARIOS QUE PARTICIPAN EN LA CÁMARA (Comprador)
    //Relación N : N con User (Una cámara tiene muchos usuarios, un usuario puede estar en muchas cámaras)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "participantes_camaras",
            joinColumns = @JoinColumn(name = "camara_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participantes;

    public Camara() {
    }

    public Direccion getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(Direccion direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public double getPrecioEnvio() {
        return precioEnvio;
    }

    public void setPrecioEnvio(double precioEnvio) {
        this.precioEnvio = precioEnvio;
    }

    public List<User> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<User> participantes) {
        this.participantes = participantes;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public User getProveedor() {
        return proveedor;
    }

    public void setProveedor(User proveedor) {
        this.proveedor = proveedor;
    }

    public User getCreador() {
        return creador;
    }

    public void setCreador(User creador) {
        this.creador = creador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaDeExpiracion() {
        return fechaDeExpiracion;
    }

    public void setFechaDeExpiracion(Date fechaDeExpiracion) {
        this.fechaDeExpiracion = fechaDeExpiracion;
    }

    public Integer getEstadoDeLaCamara() {
        return estadoDeLaCamara;
    }

    public void setEstadoDeLaCamara(int estadoDeLaCamara) {
        this.estadoDeLaCamara = estadoDeLaCamara;
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
