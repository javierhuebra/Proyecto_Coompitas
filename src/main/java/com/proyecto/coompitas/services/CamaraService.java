package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Camara;
import com.proyecto.coompitas.repositories.CamaraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamaraService {
    private final CamaraRepository camaraRepository;

    public CamaraService(CamaraRepository camaraRepository){
        this.camaraRepository = camaraRepository;
    }

    //Crear una cámara
    public Camara createCamara(Camara camara){
        return camaraRepository.save(camara);
    }

    //Traer todas las camaras creadas
    public List<Camara> allCamaras(){
        return camaraRepository.findAll();
    }

    //Buscar una camara por su id
    public Camara findCamara(Long id){
        return camaraRepository.findById(id).orElse(null);
    }

    //Buscar camaras por proveedor
    public List<Camara> findCamarasByProveedorId(Long id){
        return camaraRepository.findByProveedorId(id);
    }

    //Traer todas las camaras con estado 2
    public List<Camara> findCamarasByEstado(int estado){
        return camaraRepository.findByEstadoDeLaCamara(estado);
    }

}
