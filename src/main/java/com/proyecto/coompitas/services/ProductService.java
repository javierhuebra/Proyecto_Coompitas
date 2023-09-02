package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductoRepository productRepository;

    public ProductService(ProductoRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Crear producto
    public Producto createProduct(Producto product) {

        return productRepository.save(product);
    }
}
