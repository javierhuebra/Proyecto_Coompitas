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

    //Buscar tablas por producto y pedido
    public List<PedidoProducto> buscarPorPedido(Long idPedido){
        return  pedidoProductoRepository.findAllByPedidoId(idPedido);
    }
}
