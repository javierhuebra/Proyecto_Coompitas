package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.PedidoProducto;
import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.services.*;
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
    private final PedidoProductoService pedidoProductoService;

    ProductService productService;

    public PedidoController(PedidoService pedidoService,
                            UserService userService,
                            CamaraService camaraService,
                            ProductService productService,
                            PedidoProductoService pedidoProductoService) {
        this.pedidoService = pedidoService;
        this.userService = userService;
        this.camaraService = camaraService;
        this.productService = productService;
        this.pedidoProductoService = pedidoProductoService;
    }

    //POST PARA CADA PRODUCTO, AGREGA ESE PRODUCTO AL PEDIDO
    @PostMapping("/crear/{idProveedor}/pedido")
    public String crearPedido(@PathVariable("idProveedor") Long idProveedor,
                              @RequestParam("idProducto") Integer idProducto,
                              @RequestParam("cantidad") Integer cantidad,
                              HttpSession session
                              ){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {


            Pedido pedidoIniciado = pedidoService.buscarPeidoSinCamara(userService.findUserById(idLogueado));

            System.out.println(pedidoIniciado.getId());

            Producto productoACargar = productService.findProductById(idProducto.longValue());//Busco el producto que se quiere agregar al pedido



            PedidoProducto relacionPedido = new PedidoProducto();//Creo la relación
            relacionPedido.setCantidad(cantidad);//Le asigno la cantidad
            relacionPedido.setPedido(pedidoIniciado);//Le asigno el pedido
            relacionPedido.setProducto(productoACargar);//Le asigno el producto

            pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + (productoACargar.getPrecio() * cantidad));//Le asigno el precio total del pedido
            pedidoService.crearPedido(pedidoIniciado);//Guardo el pedido

            pedidoProductoService.crearRelacion(relacionPedido);//Guardo la relación

            return "redirect:/camara/proveedores/catalogo/"+idProveedor;
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }


    }
}
