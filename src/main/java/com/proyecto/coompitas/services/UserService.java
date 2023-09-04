package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Para registrar usuario y hashear password
    public User registerUser(User user){
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        return userRepository.save(user);
    }
    // Encontrar un usuario por su email
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    // Encontrar usuario por ID
    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    // Autenticar usuario
    public boolean authenticateUser(String email, String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return false;
        }else{
            return BCrypt.checkpw(password, user.getPassword());
        }
    }

    //Buscar usuario por rolUsuario (1 = comprador, 2 = proveedor)
    public List<User> findAllUsersByRol(int rolUser){
        return userRepository.findByRolUsuario(rolUser);
    }

    //Actualizar usuario
    public User updateUser(User user){
        return userRepository.save(user);
    }
}
