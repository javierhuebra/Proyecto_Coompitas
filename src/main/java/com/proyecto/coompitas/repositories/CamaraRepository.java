package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Camara;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamaraRepository extends CrudRepository<Camara, Long> {
    List<Camara> findAll();

    //Buscar camaras por usuario proveedor
    List<Camara> findByProveedorId(Long id);

    //Buscar camaras por estado
    List<Camara> findByEstadoDeLaCamara(int estado);

    //Buscar camaras por usuario creador
    List<Camara> findByCreadorId(Long id);

    //Buscar camaras por usuario que participa
    List<Camara> findByParticipantesId(Long id);
}
