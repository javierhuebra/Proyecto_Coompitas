package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.PedidoProducto;
import com.proyecto.coompitas.repositories.PedidoProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoProductoService {
    private final PedidoProductoRepository pedidoProductoRepository;

    public PedidoProductoService(PedidoProductoRepository pedidoProductoRepository){
        this.pedidoProductoRepository = pedidoProductoRepository;
    }

    //Crear la relación
    public PedidoProducto crearRelacion(PedidoProducto pedidoProducto){
        return pedidoProductoRepository.save(pedidoProducto);
    }

    //Buscar tablas por pedido
    public List<PedidoProducto> buscarPorPedido(Long idPedido){
        return  pedidoProductoRepository.findAllByPedidoId(idPedido);
    }

    //Buscar si hay un producto en un pedido (Si hay mas solo agarrara el primero, nuestra logica teoricamente no permite que haya mas porque se valida cuando se carga)
    public PedidoProducto buscarProductoEnPedido(Long idProducto, Long idPedido){
        return  pedidoProductoRepository.findFirstByPedidoIdAndProductoId(idPedido, idProducto);
    }
}
