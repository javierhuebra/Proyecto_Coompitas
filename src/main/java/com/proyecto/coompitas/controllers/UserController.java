package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.classes.UserPass;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.UserService;
import com.proyecto.coompitas.validator.UserValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    public UserController (UserService userService,
                           UserValidator userValidator){

        this.userService = userService;
        this.userValidator = userValidator;
    }


    @GetMapping("/registration")
    public String renderRegisterPage(@ModelAttribute("user") User usuario){
        return "ciclo_registro_login/registrationPage";
    }

    //Estos controladores estan duplicados porque no se me ocurrio una forma de hacerlo con un solo controlador por el tema de los errores de validacion
    @PostMapping("/user/register/1")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model viewModel){
        userValidator.validate(user, result);
        if(result.hasErrors()){
            System.out.println("Hay error-");
            return "ciclo_registro_login/registrationPage";
        }else{
            try{
                user.setUrlFotoPerfil("https://cdn.create.vista.com/api/media/small/259400730/stock-photo-illustration-businessman-icon-avatar-flat-design-259400730.jpg");
                user.setRolUsuario(1);
                userService.registerUser(user);
                //session.setAttribute("idLogueado", user.getId()); Esto para loguear directamente y que te mande a completar la direccion al perfil, un registro en dos pasos para mejorar UX
            }catch(DataIntegrityViolationException e){
                System.out.println("Error de integridad de datos - Email ya existe");
                viewModel.addAttribute("emailDuplicateError", "El correo electrónico no esta disponible");
                return "ciclo_registro_login/registrationPage";
            }

            return "redirect:/login";
        }
    }
    //Estos controladores estan duplicados porque no se me ocurrio una forma de hacerlo con un solo controlador por el tema de los errores de validacion
    @PostMapping("/user/register/2")
    public String registerUser2(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model viewModel){
        userValidator.validate(user, result);
        if(result.hasErrors()){
            System.out.println("Hay error-");
            return "ciclo_registro_login/registrationPage";
        }else{
            try{
                user.setUrlFotoPerfil("https://cdn.create.vista.com/api/media/small/259400730/stock-photo-illustration-businessman-icon-avatar-flat-design-259400730.jpg");
                user.setRolUsuario(2);
                userService.registerUser(user);
                //session.setAttribute("idLogueado", user.getId()); Esto para loguear directamente y que te mande a completar la direccion al perfil, un registro en dos pasos para mejorar UX
            }catch(DataIntegrityViolationException e){
                System.out.println("Error de integridad de datos - Email ya existe");
                viewModel.addAttribute("emailDuplicateError", "El correo electrónico no esta disponible");
                return "ciclo_registro_login/registrationPage";
            }

            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String renderLoginPage(@ModelAttribute("userLogin") UserPass userLogin){
        return "ciclo_registro_login/loginPage";
    }

    @PostMapping("/user/login")
    public String loginUser(@Valid @ModelAttribute("userLogin") UserPass userLogin,
                            BindingResult result, HttpSession session,
                            Model viewModel){
        //Parece que si no pongo el condicional de los errores me detecta igual la validacion, pero lo agrego igual para tener mas control
        if(result.hasErrors()){
            System.out.println("Hay error de validaciones de modelo");
            return "ciclo_registro_login/loginPage";
        }
        if(userService.authenticateUser(userLogin.getEmail(),userLogin.getPassword())){
            Long idUsuario = userService.findByEmail(userLogin.getEmail()).getId();
            session.setAttribute("idLogueado", idUsuario);

            User userLogueado = userService.findUserById(idUsuario);//Busco el usuario para saber si completó por lo menos una direccion

            if(userLogueado.getDirecciones().size() == 0){
                return "redirect:/perfil";
            }else{
                return "redirect:/home";
            }

        }else{
            System.out.println("Error de autenticacion");
            viewModel.addAttribute("error", "Credenciales inválidas.");
            return "ciclo_registro_login/loginPage";
        }
    }

    //LOGOUT (Cerrar sesion)
    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    //POST PARA CAMBIAR LA FOTO DE PERFIL
    @PostMapping("/user/perfil/foto")
    public String cambiarFotoPerfil(@RequestParam("url") String url,
                                    HttpSession session){

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);
            userLogueado.setUrlFotoPerfil(url);
            userLogueado.setPasswordConfirmation(userLogueado.getPassword());//Lo importante es poner algo, no se guarda en la base de datos
            userService.updateUser(userLogueado);
            return "redirect:/perfil";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }

}
