package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {
    List<Pedido> findAll();

    //List<Pedido> findallByComprador(User comprador);
    //Pedido findPedidoByCamaraId(Long id);

    List<Pedido> findByCompradorAndCamaraIdIsNull(User user);

}
