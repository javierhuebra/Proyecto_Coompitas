package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Direccion;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.DireccionService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DireccionController {
    private final DireccionService direccionService;
    private final UserService userService;

    public DireccionController(DireccionService direccionService, UserService userService){
        this.direccionService = direccionService;
        this.userService = userService;
    }

    //Crear una dirección para un usuario
    @PostMapping("/direccion/create")
    public String createDireccion(@Valid @ModelAttribute("direccion") Direccion direccion,
                                  BindingResult result,
                                  HttpSession session,
                                  Model viewModel
                                  ){
        //Validar la sesion activa
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado == null){
            System.out.println("No hay usuario logueado - Direccion Controller");
            return "redirect:/login";
        }

        User userLogueado = userService.findUserById(idLogueado);//Defino aca el usuario porque necesito validar para ver a donde redirecciono por las direcciones


        if (result.hasErrors()) {
            System.out.println("Hay error - Direccion Controller");
            if(userLogueado.getDirecciones().size()==0){
                viewModel.addAttribute("userLogueado", userLogueado);
                return "ciclo_funciones_generales/userProfilePage";
            }else{
                viewModel.addAttribute("direcciones", userLogueado.getDirecciones());//Inyecto solo las direcciones del usuario en la pagina adressesPage.html
                return "ciclo_funciones_generales/adressesPage";
            }

        }else{
            direccion.setUsuario(userLogueado);
            direccionService.saveDireccion(direccion);
            return "redirect:/home";
        }

    }

    //GET PARA LA PAGINA DE DIRECCIONES DEL USUARIO
    @GetMapping("/perfil/direcciones")
    public String rendrAdressesPage(@ModelAttribute("direccion") Direccion direccion, //Para crear una direccion
                                    Model viewModel,
                                    HttpSession session){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado == null){
            System.out.println("No hay usuario logueado - Direccion Controller - GET");
            return "redirect:/login";
        }

        User userLogueado = userService.findUserById(idLogueado);

        viewModel.addAttribute("direcciones", userLogueado.getDirecciones());//Inyecto solo las direcciones del usuario en la pagina adressesPage.html
        return "ciclo_funciones_generales/adressesPage";
    }
}
