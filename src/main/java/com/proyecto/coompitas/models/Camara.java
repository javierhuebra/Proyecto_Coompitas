package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "camaras")
public class Camara{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Debe seleccionar una fecha de expiración")
    private Date fechaDeExpiracion;
    @NotBlank(message = "Debe seleccionar un estado para la cámara")
    @Min(value = 0, message = "El estado de la cámara no puede ser menor a 0")
    private int estadoDeLaCamara;

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

    public Camara() {
    }

    public Camara(Long id, Date fechaDeExpiracion, int estadoDeLaCamara, Date createdAt, Date updatedAt) {
        this.id = id;
        this.fechaDeExpiracion = fechaDeExpiracion;
        this.estadoDeLaCamara = estadoDeLaCamara;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
