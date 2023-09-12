package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Camara;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerfilesController {

    private final CamaraService camaraService;
    private final UserService userService;

    public PerfilesController(CamaraService camaraService, UserService userService){
        this.camaraService = camaraService;
        this.userService = userService;
    }


    //POST PARA CONFIRMAR UNA CÁMARA (CAMBIA EL ESTADO DE LA CAMARA Y AGREGA EL PRECIO DE ENVÍO)
    @PostMapping("/perfil/camara/confirmacion/{idCamara}")
    public String confirmarCamara(@PathVariable("idCamara") Long idCamara,
                                  @RequestParam("precioEnvio") int precioEnvio,
                                  HttpSession session){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if(idLogueado != null){
            Camara camara = camaraService.findCamara(idCamara);
            camara.setPrecioEnvio(precioEnvio);
            if(camara.getEstadoDeLaCamara() == 3){
                camara.setEstadoDeLaCamara(4);
            }else{
                camara.setEstadoDeLaCamara(2);
            }
            camaraService.createCamara(camaraService.findCamara(idCamara));
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

        return "redirect:/perfil";
    }



}
