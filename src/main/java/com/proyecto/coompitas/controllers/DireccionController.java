package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Direccion;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.DireccionService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DireccionController {
    private final DireccionService direccionService;
    private final UserService userService;

    public DireccionController(DireccionService direccionService, UserService userService){
        this.direccionService = direccionService;
        this.userService = userService;
    }

    //Crear una dirección para un usuario
    @PostMapping("/direccion/create")
    public String createDireccion(@Valid @ModelAttribute("direccion") Direccion direccion,
                                  HttpSession session,
                                  BindingResult result){
        //Validar la sesion activa
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado == null){
            System.out.println("No hay usuario logueado - Direccion Controller");
            return "redirect:/login";
        }

        else if(result.hasErrors()) {
            System.out.println("Hay error - Direccion Controller");
            return "homePage";
        }else{
            User userLogueado = userService.findUserById(idLogueado);
            return "redirect:/home";
        }

    }
}
