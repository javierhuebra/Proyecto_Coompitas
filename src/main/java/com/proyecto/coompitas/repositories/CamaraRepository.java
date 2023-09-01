package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Camara;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamaraRepository extends CrudRepository<Camara, Long> {
    List<Camara> findAll();
}
