package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Direccion;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {
    private final UserService userService;

    public MainController(UserService userService){
        this.userService = userService;
    }

    //GET PARA HOME
    @GetMapping("/home")
    public String renderHome(HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null){
            User userLogueado = userService.findUserById(idLogueado);
            viewModel.addAttribute("userLogueado", userLogueado);

            if(userLogueado.getDirecciones().size() == 0){
                return "redirect:/perfil";
            }

            return "ciclo_registro_login/homePage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //GET PARA PERFIL
    @GetMapping("/perfil")
    public String renderPerfil(@Valid @ModelAttribute("direccion") Direccion direccion,
                               BindingResult result,
                               HttpSession session,
                               Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);
            viewModel.addAttribute("userLogueado", userLogueado);
            return "ciclo_funciones_generales/perfilPage";
        }
        else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }
}
