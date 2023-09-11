package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Camara;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.PedidoProductoService;
import com.proyecto.coompitas.services.PedidoService;
import com.proyecto.coompitas.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PagosController {
    private final CamaraService camaraService;
    private final UserService userService;
    private final PedidoProductoService pedidoProductoService;
    private final PedidoService pedidoService;

    public PagosController(CamaraService camaraService,
                                UserService userService,
                                PedidoProductoService pedidoProductoService,
                                PedidoService pedidoService) {
        this.camaraService = camaraService;
        this.userService = userService;
        this.pedidoProductoService = pedidoProductoService;
        this.pedidoService = pedidoService;
    }

    //POST PARA CAMBIAR EL ESTADO DE LA CAMARA PARA QUE SE PUEDA PAGAR
    @PostMapping("/camara/{idCamara}/estadoDePago")
    public String cambiarEstadoDePago(@PathVariable("idCamara") Long idCamara){
        Camara camara = camaraService.findCamara(idCamara);
        if(camara.getEstadoDeLaCamara() == 3){
            camara.setEstadoDeLaCamara(2);
        }else{
            camara.setEstadoDeLaCamara(3);
        }

        camaraService.createCamara(camara);
        return "redirect:/camara/"+idCamara;
    }
}
