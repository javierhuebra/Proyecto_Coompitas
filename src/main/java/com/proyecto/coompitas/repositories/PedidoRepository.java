package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Pedido;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {
    //List<Pedido> findallByComprador(User comprador);
    //Pedido findPedidoByCamaraId(Long id);

}
