package com.proyecto.coompitas.services;

import com.proyecto.coompitas.repositories.CantDescRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CantDescService {
    private final CantDescRepository cantDescRepository;

    public CantDescService(CantDescRepository cantDescRepository){
        this.cantDescRepository = cantDescRepository;
    }
    //Eliminar todos los registros por id de producto
    @Transactional//Esto se puso porque no me dejaba eliminar los registros de la tabla cant_desc cuando queria editar el producto(Todo surge a raiz de como se estructuraron las cantidades y descuentos)
    public void deleteAllByProductoId(Long id){
        cantDescRepository.deleteAllByProductoId(id);
    }


}
