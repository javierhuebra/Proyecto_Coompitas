package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.Direccion;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.CamaraService;
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
    private final CamaraService camaraService;
    public MainController(UserService userService, CamaraService camaraService){
        this.userService = userService;
        this.camaraService = camaraService;
    }

    //GET PARA HOME
    @GetMapping("/home")
    public String renderHome(HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null){
            User userLogueado = userService.findUserById(idLogueado);
            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado en el modelo para que se pueda usar en la página homePage

            viewModel.addAttribute("camarasCreadas", camaraService.findCamarasByEstado(2));//Inserto las camaras aceptadas

            if(userLogueado.getDirecciones().size() == 0){//Si no tiene direcciones (cuando recien se registra es) es redirigido al perfil para que cargue una
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
    public String renderPerfil(@ModelAttribute("direccion") Direccion direccion,
                               HttpSession session,
                               Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);
            viewModel.addAttribute("userLogueado", userLogueado);
            if(userLogueado.getRolUsuario() == 2){
                viewModel.addAttribute("camarasProveidas", camaraService.findCamarasByProveedorId(idLogueado));
            }

            return "ciclo_funciones_generales/userProfilePage";
        }
        else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }
}
