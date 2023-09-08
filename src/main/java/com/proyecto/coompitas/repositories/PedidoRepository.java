package com.proyecto.coompitas.repositories;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {
    List<Pedido> findAll();

    //List<Pedido> findallByComprador(User comprador);
    //Pedido findPedidoByCamaraId(Long id);

    Pedido findFirstByCompradorAndCamaraIdIsNull(User user);//Buscar pedidos de un usuario que no tienen camara asignada aun

    //@Query("SELECT DISTINCT p FROM Pedido ped JOIN ped.productos p WHERE ped.id = :idPedido")
    //List<Producto> findDistinctProductosById(@Param("idPedido") Long idPedido);

    //@Query("SELECT DISTINCT producto FROM Pedido p JOIN p.productos producto")
    //List<Producto> findProductosDistintosEnPedidos();

    int countProductosByProductosContaining(Producto producto);

    List<Pedido> findAllByCamaraId(Long idCamara);



}
