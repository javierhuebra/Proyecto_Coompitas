package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.CantDesc;
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


            PedidoProducto relacionPedidoExistente = pedidoProductoService.buscarProductoEnPedido(productoACargar.getId(), pedidoIniciado.getId());//Busco si ya hay una relación con ese producto en el pedido
            if(relacionPedidoExistente != null){//Si ya hay una relación con ese producto en el pedido
                System.out.println("Ya hay una relación con ese producto en el pedido");

                relacionPedidoExistente.setCantidad(relacionPedidoExistente.getCantidad() + cantidad);//Le sumo la cantidad

                //aca hay que hacer la validación de la cantidad y los descuentos aplicables //Cambiarlo por un for mejorado
                double porcentualDescuento=0;
                double porcentajeDescuentoPreseteado = productoACargar.getPrecio() * cantidad * (relacionPedidoExistente.getDescuentoVigente()/100);//El descuento que arrastra de la primera vez que se declaró el grupo de productos
                System.out.println("descuento vigente: "+porcentajeDescuentoPreseteado);

                pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + (productoACargar.getPrecio() * cantidad));//Le asigno el precio total del pedido
                //relacionPedidoExistente.setPrecioProductos(relacionPedidoExistente.getPrecioProductos()+(productoACargar.getPrecio() * cantidad));//Le asigno el precio que no contiene descuentos a la relación que tiene la cantidad de productos

                for(CantDesc cantDesc :productoACargar.getCantidadesDescuentos()){
                    if((relacionPedidoExistente.getCantidad()+cantidad) >= cantDesc.getCantidad()) {
                        double porcentajeDescuentoTemp = productoACargar.getPrecio() * cantidad * (porcentualDescuento / 100);
                        double precioSinDescuento = productoACargar.getPrecio() * cantidad;

                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() - (precioSinDescuento - porcentajeDescuentoTemp));//Le asigno el precio total del pedido

                        porcentualDescuento = cantDesc.getDescuentoAplicado();//Ahora modifico el porcentual de descuento para sumarle al total el precio correcto

                        //hacer condicional que valide si el descuento que se le aplica es mayor al que ya tiene
                        if (relacionPedidoExistente.getDescuentoVigente() > porcentualDescuento) {
                            porcentualDescuento = relacionPedidoExistente.getDescuentoVigente();
                        } else if (relacionPedidoExistente.getDescuentoVigente() < porcentualDescuento) {
                            int cantidadAreformular = relacionPedidoExistente.getCantidad() - cantidad;
                            double precioAcarreadoAreformular = productoACargar.getPrecio() * cantidadAreformular - (productoACargar.getPrecio() * cantidadAreformular * (porcentualDescuento / 100));
                            relacionPedidoExistente.setPrecioProductos(precioAcarreadoAreformular);
                        }


                        double precioConDescuentoQueSeAgregan = (productoACargar.getPrecio() * cantidad) - (productoACargar.getPrecio() * cantidad * (porcentualDescuento / 100));


                        relacionPedidoExistente.setDescuentoVigente(porcentualDescuento);
                        relacionPedidoExistente.setPrecioProductos(precioConDescuentoQueSeAgregan);
                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + precioConDescuentoQueSeAgregan);
                    }
                }

                pedidoService.crearPedido(pedidoIniciado);//Guardo el pedido
                pedidoProductoService.crearRelacion(relacionPedidoExistente);//Guardo la relación

                return "redirect:/camara/proveedores/catalogo/"+idProveedor;
            }else{
                PedidoProducto relacionPedido = new PedidoProducto();//Creo la relación
                relacionPedido.setCantidad(cantidad);//Le asigno la cantidad
                relacionPedido.setPedido(pedidoIniciado);//Le asigno el pedido
                relacionPedido.setProducto(productoACargar);//Le asigno el producto

                //aca hay que hacer la validación de la cantidad y los descuentos aplicables

                pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + (productoACargar.getPrecio() * cantidad));//Le asigno el precio total del pedido
                relacionPedido.setPrecioProductos((productoACargar.getPrecio() * cantidad));//Le asigno el precio que no contiene descuentos a la relación que tiene la cantidad de productos
                double porcentualDescuento=0;
                for(CantDesc cantDesc :productoACargar.getCantidadesDescuentos()){
                    if(cantidad >= cantDesc.getCantidad()){

                        double porcentajeDescuentoTemp = productoACargar.getPrecio() * cantidad * (porcentualDescuento/100);
                        double precioSinDescuento = productoACargar.getPrecio() * cantidad;

                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal()-(precioSinDescuento-porcentajeDescuentoTemp));//Le asigno el precio total del pedido

                        porcentualDescuento = cantDesc.getDescuentoAplicado();//Ahora modifico el porcentual de descuento para sumarle al total el precio correcto

                        System.out.println("Porcentual descuento: "+porcentualDescuento);
                        double precioConDescuentoPeidosIguales = (productoACargar.getPrecio() * cantidad) - (productoACargar.getPrecio() * cantidad * (porcentualDescuento/100));

                        relacionPedido.setDescuentoVigente(porcentualDescuento);
                        relacionPedido.setPrecioProductos(precioConDescuentoPeidosIguales);
                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + precioConDescuentoPeidosIguales);
                    }
                }

                pedidoService.crearPedido(pedidoIniciado);//Guardo el pedido
                pedidoProductoService.crearRelacion(relacionPedido);//Guardo la relación

                return "redirect:/camara/proveedores/catalogo/"+idProveedor;
            }


        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }


    }
}
