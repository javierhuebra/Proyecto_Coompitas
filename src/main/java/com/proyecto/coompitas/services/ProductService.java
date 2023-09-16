package com.proyecto.coompitas.services;

import com.proyecto.coompitas.models.Producto;
import com.proyecto.coompitas.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //Buscar producto por id
    public Producto findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    //Traer todos los productos
    public List<Producto> allProducts() {
        return productRepository.findAll();
    }

    //Eliminar producto por id
    public void eliminarProducto(Long id){
        productRepository.deleteById(id);
    }

}
