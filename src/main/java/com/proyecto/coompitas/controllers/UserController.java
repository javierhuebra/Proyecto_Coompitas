package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.classes.UserPass;
import com.proyecto.coompitas.models.User;
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
public class UserController {
    private final UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }



    @GetMapping("/registration")
    public String renderRegisterPage(@ModelAttribute("user") User usuario){
        return "registrationPage";
    }

    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result){
        if(result.hasErrors()){
            System.out.println("Hay error");
            return "registrationPage";
        }else{
            userService.registerUser(user);
            //session.setAttribute("idLogueado", user.getId());

            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String renderLoginPage(@ModelAttribute("userLogin") UserPass userLogin){
        return "loginPage";
    }

    @PostMapping("/user/login")
    public String loginUser(@Valid @ModelAttribute("userLogin") UserPass userLogin,
                            BindingResult result, HttpSession session,
                            Model viewModel){
        if(userService.authenticateUser(userLogin.getEmail(),userLogin.getPassword())){
            session.setAttribute("idLogueado", userService.findByEmail(userLogin.getEmail()));
            return "redirect:/home";
        }else{
            System.out.println("Error de logeo");
            return "loginPage";
        }
    }


}
