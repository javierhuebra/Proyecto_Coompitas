package com.proyecto.coompitas.validator;

import com.proyecto.coompitas.classes.UserPass;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass){
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors){
        User user = (User) object;

        if(!user.getPassword().equals(user.getPasswordConfirmation())){
            errors.rejectValue("passwordConfirmation", "Match", "Password y Password Confirmation deben ser iguales");
        }

    }

}
