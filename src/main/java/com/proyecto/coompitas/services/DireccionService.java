package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Direccion;
import com.proyecto.coompitas.repositories.DireccionRepository;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository){
        this.direccionRepository = direccionRepository;
    }

    //Guardar direccion
    public Direccion saveDireccion(Direccion direccion){
        return direccionRepository.save(direccion);
    }

    //Buscar direccion por id
    public Direccion findDireccionById(Long id){
        return direccionRepository.findById(id).orElse(null);
    }
}
