package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends CrudRepository<Producto,Long> {
    //Buscar todos los productos
    List<Producto> findAll();

    //Buscar todos los productos de un usuario
    List<Producto> findAllByProveedor(User proveedor);
}
