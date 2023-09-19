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

    //Eliminar direccion
    public void deleteDireccion(Long id){//Esto no elimina la dirección pero le quita elusuario que la tiene asignada asi no se rompe la relación y la camara existe igual
        Direccion direccion = direccionRepository.findById(id).orElse(null);
        if(direccion != null){
            direccion.setUsuario(null);
            direccionRepository.save(direccion);
        }
    }
}
