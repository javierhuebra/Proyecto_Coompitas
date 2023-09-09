package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Pedido;
import com.proyecto.coompitas.models.Producto;
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

    public Pedido buscarPeidoSinCamara(User user){
        return pedidoRepository.findFirstByCompradorAndCamaraIdIsNull(user);
    }

    public List<Pedido> buscarPedidosPorCamara(Long idCamara){
        return pedidoRepository.findAllByCamaraId(idCamara);
    }

    //Buscar pedido por id
    public Pedido buscarPedidoPorId(Long id){
        return pedidoRepository.findById(id).orElse(null);
    }

}
