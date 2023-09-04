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

                pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() + (productoACargar.getPrecio() * cantidad));//Le asigno el precio total del pedido

                for(CantDesc cantDesc :productoACargar.getCantidadesDescuentos()){
                    if((relacionPedidoExistente.getCantidad()+cantidad) >= cantDesc.getCantidad()){
                        //aca hay que usar la cantidd almacenada en relacion existente
                        double porcentajeDescuentoTemp = productoACargar.getPrecio() * cantidad * (porcentualDescuento/100);
                        double precioSinDescuento = productoACargar.getPrecio() * cantidad;

                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() - (productoACargar.getPrecio() * (porcentualDescuento/100) * cantidad));//Le asigno el precio total del pedido

                        porcentualDescuento = cantDesc.getDescuentoAplicado();//Ahora modifico el porcentual de descuento para sumarle al total el precio correcto

                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() +(productoACargar.getPrecio() * cantidad)- (productoACargar.getPrecio() * (porcentualDescuento/100) * cantidad));
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

                double porcentualDescuento=0;
                for(CantDesc cantDesc :productoACargar.getCantidadesDescuentos()){
                    if(cantidad >= cantDesc.getCantidad()){

                        double porcentajeDescuentoTemp = productoACargar.getPrecio() * cantidad * (porcentualDescuento/100);
                        double precioSinDescuento = productoACargar.getPrecio() * cantidad;

                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal()-(precioSinDescuento-porcentajeDescuentoTemp));//Le asigno el precio total del pedido

                        porcentualDescuento = cantDesc.getDescuentoAplicado();//Ahora modifico el porcentual de descuento para sumarle al total el precio correcto
                        System.out.println("Porcentual descuento: "+porcentualDescuento);
                        pedidoIniciado.setPrecioTotal(pedidoIniciado.getPrecioTotal() +(productoACargar.getPrecio() * cantidad) - (productoACargar.getPrecio() * cantidad * (porcentualDescuento/100)));
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
