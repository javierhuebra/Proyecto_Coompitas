package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    public PedidoService(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public void crearPedido(Pedido pedido){
        pedidoRepository.save(pedido);
    }
}
