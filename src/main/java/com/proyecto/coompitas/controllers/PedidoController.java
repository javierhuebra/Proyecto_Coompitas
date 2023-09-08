package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.*;
import com.proyecto.coompitas.services.*;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                              HttpSession session, Model viewModel
    ) {
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {

            Camara camaraVigente = (Camara) session.getAttribute("camara");
            int cantidadArestar = 0;
            if (camaraVigente != null) { //SI HAY UNA CAMARA VIGENTE (Se esta uniendo alguien a la camara)
                System.out.println("Anexando comprador");

                List<Pedido> pedidosEnCamara = pedidoService.buscarPedidosPorCamara(camaraVigente.getId());//Guardo los pedidos que tiene la camara para poder evaluar las cantidades de productos solicitadas en la camara
                System.out.println(pedidosEnCamara.size());
                for (Pedido pedido : pedidosEnCamara) {//Busco los registros de relacion entre pedido y producto para cada pedido de la camara
                    PedidoProducto registrosDelPedidoYProducto = pedidoProductoService.buscarProductoEnPedido(idProducto.longValue(), pedido.getId());//Accedo al registro de pedido producto que tiene la cantidad de ese producto que hay en la camara
                    cantidad = cantidad + registrosDelPedidoYProducto.getCantidad();//Le sumo la cantidad que hay en el pedido a la cantidad que se quiere agregar, como es un bucle se llena con la cantidad de ese producto que tienen todos los pedidos
                    cantidadArestar += registrosDelPedidoYProducto.getCantidad();

                }
            } else {
                System.out.println("Creando comprador propietario");

            }

            Pedido pedidoIniciado = pedidoService.buscarPeidoSinCamara(userService.findUserById(idLogueado));

            System.out.println(pedidoIniciado.getId());

            Producto productoACargar = productService.findProductById(idProducto.longValue());//Busco el producto que se quiere agregar al pedido

            PedidoProducto relacionPedidoExistente = pedidoProductoService.buscarProductoEnPedido(productoACargar.getId(), pedidoIniciado.getId());//Busco si ya hay una relación con ese producto en el pedido
            PedidoProducto relacionPedido = new PedidoProducto();//Creo la relación
            if (relacionPedidoExistente != null) {//Si ya hay una relación con ese producto en el pedido
                System.out.println("Ya hay una relación con ese producto en el pedido");

                pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() - relacionPedidoExistente.getPrecioProductos());//Le resto el precio de la relación que ya estaba

                relacionPedido = relacionPedidoExistente;//Le asigno la relación existente

            }

            relacionPedido.setCantidad(cantidad);//Le asigno la cantidad

            relacionPedido.setPedido(pedidoIniciado);//Le asigno el pedido
            relacionPedido.setProducto(productoACargar);//Le asigno el producto


            pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + (productoACargar.getPrecio() * cantidad));//Le asigno el precio total del pedido

            relacionPedido.setPrecioProductos((productoACargar.getPrecio() * cantidad));//Le asigno el precio que no contiene descuentos a la relación que tiene la cantidad de productos

            double porcentualDescuento = 0;
            for (CantDesc cantDesc : productoACargar.getCantidadesDescuentos()) {
                if (cantidad >= cantDesc.getCantidad()) {

                    double porcentajeDescuentoTemp = productoACargar.getPrecio() * cantidad * (porcentualDescuento / 100);
                    double precioSinDescuento = productoACargar.getPrecio() * cantidad;


                    System.out.println(pedidoIniciado.getPrecioTotal());
                    pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() - (precioSinDescuento - porcentajeDescuentoTemp));//Le asigno el precio total del pedido

                    porcentualDescuento = cantDesc.getDescuentoAplicado();//Ahora modifico el porcentual de descuento para sumarle al total el precio correcto

                    System.out.println("Porcentual descuento: " + porcentualDescuento);
                    double precioConDescuentoPedidosIguales = (productoACargar.getPrecio() * cantidad) - (productoACargar.getPrecio() * cantidad * (porcentualDescuento / 100));


                    relacionPedido.setDescuentoVigente(porcentualDescuento);
                    relacionPedido.setPrecioProductos(precioConDescuentoPedidosIguales);
                    pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + precioConDescuentoPedidosIguales);

                } else {
                    relacionPedido.setDescuentoVigente(porcentualDescuento);
                }
            }

            pedidoService.crearPedido(pedidoIniciado);//Guardo el pedido
            pedidoProductoService.crearRelacion(relacionPedido);//Guardo la relación


            if (camaraVigente != null) {
                return "redirect:/unirse/" + camaraVigente.getId() + "/productos";
            }

            return "redirect:/camara/proveedores/catalogo/" + idProveedor;


        } else {
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }


    }
}
