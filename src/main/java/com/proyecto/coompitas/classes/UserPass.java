package com.proyecto.coompitas.classes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserPass {
    @Email(message = "El mail no cumple el formato requerido")
    @NotBlank(message = "El email no puede ser nulo")
    private String email;
    @NotBlank(message = "La contraseña no puede ser nula")
    private String password;

    public UserPass() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
