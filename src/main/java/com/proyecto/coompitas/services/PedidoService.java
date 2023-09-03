package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.User;
import com.proyecto.coompitas.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    public PedidoService(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public void crearPedido(Pedido pedido){
        pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPeidoSinCamara(User user){
        return pedidoRepository.findByCompradorAndCamaraIdIsNull(user);
    }
}
