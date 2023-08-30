package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El país no puede ser nulo")
    @Size(min = 3, max = 120, message = "El país debe tener entre 3 y 120 caracteres")
    private String pais;
    @NotBlank(message = "La provincia no puede ser nula")
    @Size(min = 3, max = 120, message = "La provincia debe tener entre 3 y 120 caracteres")
    private String provincia;
    @NotBlank(message = "La ciudad no puede ser nula")
    @Size(min = 3, max = 120, message = "La ciudad debe tener entre 3 y 120 caracteres")
    private String ciudad;
    @NotBlank(message = "El código postal no puede ser nulo")
    @Size(min = 3, max = 30, message = "El código postal debe tener entre 3 y 30 caracteres")
    private String codigoPostal;
    @NotBlank(message = "La calle no puede ser nula")
    @Size(min = 3, max = 120, message = "La calle debe tener entre 3 y 120 caracteres")
    private String calle;

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

    public Direccion() {
    }

    public Direccion(Long id, String pais, String provincia, String ciudad, String codigoPostal, String calle) {
        this.id = id;
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
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
}
