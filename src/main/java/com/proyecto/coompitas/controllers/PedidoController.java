package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.PedidoService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PedidoController {
    private final PedidoService pedidoService;
    private final UserService userService;

    private final CamaraService camaraService;

    public PedidoController(PedidoService pedidoService, UserService userService, CamaraService camaraService){
        this.pedidoService = pedidoService;
        this.userService = userService;
        this.camaraService = camaraService;
    }

    //POST PARA CADA PRODUCTO, AGREGA ESE PRODUCTO AL PEDIDO
    @PostMapping("/crear/{idProveedor}/pedido")
    public String crearPedido(@PathVariable("idProveedor") Long idProveedor,
                              @ModelAttribute("producto") Producto producto,
                              @RequestParam("cantidad") Integer cantidad,
                              HttpSession session
                              ){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {


            List<Pedido> pedidoIniciado = pedidoService.buscarPeidoSinCamara(userService.findUserById(idLogueado));
            System.out.println(pedidoIniciado.get(0).getId());
            System.out.println(producto.getNombre());
            for(int i = 0; i < cantidad; i++){//Agrego la cantidad de productos que se pidieron
              pedidoIniciado.get(0).getProductos().add(producto);
            }

            pedidoService.crearPedido(pedidoIniciado.get(0));//Guardo el pedido en la base de datos

            return "redirect:/camara/proveedores/catalogo/"+idProveedor;
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }


    }
}
