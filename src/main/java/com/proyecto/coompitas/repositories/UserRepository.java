package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    List<User> findAll();

    User findByEmail(String email);

    //Buscar usuario por rolUsuario
    List<User> findByRolUsuario(int rolUser);
}
