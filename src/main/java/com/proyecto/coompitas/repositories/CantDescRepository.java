package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.CantDesc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantDescRepository extends CrudRepository<CantDesc, Long> {
    //Eliminar todos los registros por id de producto
    void deleteAllByProductoId(Long id);
}
