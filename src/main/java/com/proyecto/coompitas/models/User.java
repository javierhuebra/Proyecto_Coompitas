package com.proyecto.coompitas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la empresa no puede ser nulo")
    private String nombreEmpresa;
    @NotBlank(message = "El CUIT/CUIL no puede ser nulo")
    private String cuitCuil;
    @NotBlank(message = "El nombre del titular no puede ser nulo")
    private String nombreTitular;
    @Min(value = 1, message = "Debe seleccionar un rol de usuario")
    @Max(value = 2, message = "Debe seleccionar un rol de usuario")
    private int rolUsuario;
    @Email(message = "El mail no cumple el formato requerido")
    @NotBlank(message = "El email no puede ser nulo")
    @Column(name ="email",unique = true)
    private String email;
    @NotBlank(message = "El teléfono de contacto no puede ser nulo")
    private String telefono;
    @NotBlank(message = "La contraseña no puede ser nula")
    private String password;
    @Transient
    @NotBlank(message = "La confirmación de contraseña no puede ser nula")
    private String passwordConfirmation;
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    //DIRECCIONES
    //Relación 1 : N con Direccion (Un usuario puede tener muchas direcciones, una dirección tiene un solo usuario)
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Direccion> direcciones;


    //CREACION DE CAMARAS (Creador)
    //Relación 1 : N con Camara (Un usuario creador puede tener muchas cámaras, una cámara tiene un solo usuario)
    @OneToMany(mappedBy = "creador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Camara> camarasCreadas;

    //CREACION DE CAMARAS (Proveedor)
    //Relación 1 : N con Camara (Un usuario proveedor puede proveer muchas cámaras, una cámara tiene un solo proveedor)
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Camara> camarasProveidas;


    public User() {
    }

    public List<Camara> getCamarasProveidas() {
        return camarasProveidas;
    }

    public void setCamarasProveidas(List<Camara> camarasProveidas) {
        this.camarasProveidas = camarasProveidas;
    }

    public List<Camara> getCamarasCreadas() {
        return camarasCreadas;
    }

    public void setCamarasCreadas(List<Camara> camarasCreadas) {
        this.camarasCreadas = camarasCreadas;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCuitCuil() {
        return cuitCuil;
    }

    public void setCuitCuil(String cuitCuil) {
        this.cuitCuil = cuitCuil;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public int getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(int rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
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
