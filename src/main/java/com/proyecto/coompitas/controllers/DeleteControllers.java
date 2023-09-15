package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.PedidoService;
import com.proyecto.coompitas.services.ProductService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteControllers {
    private final UserService userService;
    private final CamaraService camaraService;
    private final PedidoService pedidoService;
    private final ProductService productService;

    public DeleteControllers(UserService userService,
                             CamaraService camaraService,
                             PedidoService pedidoService,
                             ProductService productService){
        this.userService = userService;
        this.camaraService = camaraService;
        this.pedidoService = pedidoService;
        this.productService = productService;
    }

    //POST PARA ANULAR UNA CÁMARA POR CREADOR
    @PostMapping("/camara/creador/anular/{idCamara}")
    public String anularCamara(@PathVariable("idCamara") Long idCamara,
                               HttpSession session){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado == null) {
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
        camaraService.anularCamara(idCamara,-1);
        camaraService.createCamara(camaraService.findCamara(idCamara));
        return "redirect:/perfil";
    }
    //POST PARA ANULAR UNA CÁMARA POR PROVEEDOR (ANTES DE QUE SE INICIE FUERTE, CANCELACION PERMITIDA)
    @PostMapping("/camara/proveedor/anular/{idCamara}")
    public String anularCamaraProveedor(@PathVariable("idCamara") Long idCamara,
                               HttpSession session){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado == null) {
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
        camaraService.anularCamara(idCamara,-2);
        camaraService.createCamara(camaraService.findCamara(idCamara));
        return "redirect:/perfil";
    }

}
