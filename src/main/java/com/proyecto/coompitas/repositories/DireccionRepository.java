package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Direccion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends CrudRepository<Direccion, Long> {

}
