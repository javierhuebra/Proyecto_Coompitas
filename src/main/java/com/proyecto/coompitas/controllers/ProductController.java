package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.classes.Constantes;
import com.proyecto.coompitas.models.CantDesc;
import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.CantDescService;
import com.proyecto.coompitas.services.ProductService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final CantDescService cantDescService;

    public ProductController(ProductService productService, UserService userService, CantDescService cantDescService) {
        this.productService = productService;
        this.userService = userService;
        this.cantDescService = cantDescService;
    }

    //GET PARA PESTAÑA CREAR PRODUCTOS
    @GetMapping("/perfil/productos")
    public String renderPaginaUserProductos(@ModelAttribute("producto") Producto producto,
                                            HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if(idLogueado != null){

            User userLogueado = userService.findUserById(idLogueado);
            if(userLogueado.getRolUsuario() == 2){
                viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario en la view
                viewModel.addAttribute("categorias", Constantes.categorias);//Inserto las categorias al modelo
                //viewModel.addAttribute("productos", productService.allProductsByUser());
                return "ciclo_funciones_generales/productsPage";
            }else{
                System.out.println("El usuario logueado no es un proveedor");
                return "redirect:/home";
            }
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }

    //POST PARA CREAR PRODUCTOS
    @PostMapping("/perfil/productos/crear")
    public String createProduct(@Valid @ModelAttribute("producto") Producto producto,
                                BindingResult result,
                                HttpSession session,
                                Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if(idLogueado != null){
            if(result.hasErrors()){
                System.out.println("Hay error de validacion en la creacion del producto");
                System.out.println(result.getAllErrors());
                viewModel.addAttribute("userLogueado", userService.findUserById(idLogueado));//Inserto el usuario en la view
                viewModel.addAttribute("categorias", Constantes.categorias);//Inserto las categorias al modelo
                //Producto productoVacio = new Producto();//Habia hecho esto pero el producto ya esta en el modelatribute en los parametros
                //viewModel.addAttribute("producto", productoVacio);//Entonces si ponia esto no me saltaban los erroes, como que venian vacios, se ve que los erroes vienen en el modelo por eso no aparecian con esto, lo dejo para que quede constancia nomas pero no hay que decomentar esto
                return "ciclo_funciones_generales/productsPage";
            }
            producto.setProveedor(userService.findUserById(idLogueado));

            if(producto.getId() != null){
                System.out.println("No puede haber mas de 3 cantidades y descuentos");//Esto pasa porque esta mal diagramado el programa y no sobre escribe cuando edita, hay que refactorizar, se queda asi para el MVP

                cantDescService.deleteAllByProductoId(producto.getId());

            }

            //Recorrer la lista de cantidades y descuentos y setearle el producto a cada uno
            for(int i = 0; i < producto.getCantidadesDescuentos().size(); i++){
                System.out.println("Cantidad "+i+": "+producto.getCantidadesDescuentos().get(i).getCantidad());
                System.out.println(producto.getCantidadesDescuentos().size());
                producto.getCantidadesDescuentos().get(i).setProducto(producto);
                //Falta hacer un if para validar las cantidades y descuentos que tengan sentido
            }

            productService.createProduct(producto);
            return "redirect:/perfil/productos";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }

    //GET PARA RENDERIZAR LA VIEW DE TODOS LOS PRODUCTOS
    @GetMapping("/catalogo/all/productos")
    public String renderAllProducts(Model viewModel, HttpSession session){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
        if(idLogueado != null){
            viewModel.addAttribute("productos", productService.allProducts());
            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario en la view


            return "paginas_comprador/catalogoProductosAllPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //GET PARA RENDERIZAR EL EDIT DE LOS PRODUCTOS //Estan puestos re mal los if, lo hice apurado juaasda
    @GetMapping("/perfil/productos/editar/{idProducto}")
    public String renderEditProduct(@PathVariable("idProducto") Long idProducto,
                                    Model viewModel,
                                    HttpSession session){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if(idLogueado != null){
            Producto producto = productService.findProductById(idProducto);
            User userLogueado = userService.findUserById(idLogueado);


            if(producto != null){

                //producto.getCantidadesDescuentos().clear();//Limpio la lista de cantidades y descuentos para que no se repitan al editar (esto esta mal desde el momento que definimos las cantidades y descuentos, hay que refacorizar para hacer un buen codigo)

                viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario en la view
                viewModel.addAttribute("categorias", Constantes.categorias);//Inserto las categorias al modelo
                viewModel.addAttribute("producto", producto);//Inserto el producto en el modelo
                return "ciclo_funciones_generales/productsPage";

            }else{
                System.out.println("No se encontro el producto");
                return "redirect:/perfil/productos";
            }
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }
    //DELETE PARA ELIMINAR UN PRODUCTO
    @DeleteMapping("/eliminar/{idProducto}")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto,
                                   HttpSession session){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if(idLogueado != null){
            productService.eliminarProducto(idProducto);
            return "redirect:/perfil/productos";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }
}
