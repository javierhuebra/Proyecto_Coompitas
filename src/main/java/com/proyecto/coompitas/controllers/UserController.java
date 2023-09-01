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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model viewModel){
        userValidator.validate(user, result);
        if(result.hasErrors()){
            System.out.println("Hay error-");
            return "ciclo_registro_login/registrationPage";
        }else{
            try{
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

    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

}
