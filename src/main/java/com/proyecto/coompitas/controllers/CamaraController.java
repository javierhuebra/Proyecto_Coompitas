package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Camara;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CamaraController {

    private final UserService userService;
    private final CamaraService camaraService;

    public CamaraController(UserService userService,
                            CamaraService camaraService){
        this.userService = userService;
        this.camaraService = camaraService;
    }


    //GET PARA RENDERIZAR LA PÁGINA DE LOS PROVEEDORES PARA CREAR LA CAMARA
    @GetMapping("/camara/proveedores")
    public String renderProveedores(HttpSession session, Model viewModel){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);
            if(userLogueado.getRolUsuario()!= 1){
                System.out.println("No es un comprador, vuelve a home");
                return "redirect:/home";
            }

            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado a la pagina renderProveedoresPage
            viewModel.addAttribute("usersProveedor", userService.findAllUsersByRol(2));//Inserto todos los usuarios con rol proveedor en el modelo

            return "paginas_comprador/renderProveedoresPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //GET PARA LOS PRODUCTOS DE CADA PROVEEDOR (PARA CREAR LA CAMARA)
    @GetMapping("/camara/proveedores/catalogo/{idProveedor}")
    public String renderCatalogoProveedores(@PathVariable("idProveedor") Long idProveedor,
                                            @ModelAttribute("camara") Camara camara,//Podria pasar que primero se carguen el y despues se cree la camara, hay que ver... en proceso
                                            HttpSession session,
                                            Model viewModel){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
            User userProveedor = userService.findUserById(idProveedor);//Busco el usuario proveedor para acceder a sus productos y demas datos

            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado a la pagina
            viewModel.addAttribute("userProveedor", userProveedor);//Inserto el usuario proveedor a la pagina
            return "paginas_comprador/catalogoProveedorPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }

    //POST PARA CREAR UNA CAMARA - En proceso
     @PostMapping("/camara/proveedores/crear/{idProveedor}")
     public String crearCamara(@PathVariable("idProveedor") Long idProveedor,
                               @ModelAttribute("camara") Camara camara,
                               HttpSession session){
         Long idLogueado = (Long) session.getAttribute("idLogueado");
         if (idLogueado != null) {
             User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
             User userProveedor = userService.findUserById(idProveedor);//Busco el usuario proveedor

             camara.setCreador(userLogueado);//Seteo el usuario logueado como creador de la camara
             camara.setProveedor(userProveedor);//Seteo el usuario proveedor como proveedor de la camara, los otros valores vinieron en el modelo con el formulario
             camara.setEstadoDeLaCamara(1);

            //amara.getPedidos().get(0).setCamara(camara);//Seteo la camara en el pedido

             camaraService.createCamara(camara);//Creo la camara
         }
            return "redirect:/home";
     }
}
