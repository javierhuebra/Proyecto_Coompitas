package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Camara;
import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.PedidoProducto;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.PedidoProductoService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CamaraControllerView {

    private final CamaraService camaraService;
    private final UserService userService;
    private final PedidoProductoService pedidoProductoService;

    public CamaraControllerView(CamaraService camaraService,
                                UserService userService,
                                PedidoProductoService pedidoProductoService){
        this.camaraService = camaraService;
        this.userService = userService;
        this.pedidoProductoService = pedidoProductoService;
    }
    //GET PARA RENDERIZAR LA PÁGINA DE DETALLE DE LA CAMARA
    @GetMapping("/camara/{idCamara}")
    public String renderCamara(@PathVariable("idCamara") Long idCamara,
                               HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            Camara camaraActual = camaraService.findCamara(idCamara);
            viewModel.addAttribute("camara", camaraActual);//Inserto la camara en el modelo para que se pueda usar en la página camaraPage
            viewModel.addAttribute("userLogueado", userService.findUserById(idLogueado));//Inserto el usuario logueado en el modelo para que se pueda usar en la página camaraPage

            List<Pedido> pedidosDeLaCamara = camaraActual.getPedidos();//Guardo los pedidos que tiene la camara
            List<PedidoProducto> registrosConProductoYCantidad = new ArrayList<>();//Creo una lista de PedidoProducto, que es la tabla intermedia que se genera cuando se guarda un producto en un pedido, pero le agregamos a mano la cantidad y la necesito tambien

            for(Pedido pedido : pedidosDeLaCamara){
                registrosConProductoYCantidad.addAll(pedidoProductoService.buscarPorPedido(pedido.getId()));//Agrego a la lista los registros de la tabla intermedia que se generan cuando se guarda un producto en un pedido
            }
            System.out.println(registrosConProductoYCantidad.size());
            System.out.println("asdasdas");

            viewModel.addAttribute("registrosProductoYCantidad", registrosConProductoYCantidad);//Inserto la lista de registros de la tabla intermedia en el modelo para que se pueda usar en la página camaraPage

            return "paginas_comprador/camaraPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }
}
