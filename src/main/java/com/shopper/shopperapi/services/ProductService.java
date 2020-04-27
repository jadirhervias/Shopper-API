package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.repositories.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Método para listar usuarios
     * @return List<Product>
     */
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    /**
     * Método para buscar producto por id
     * @param id
     * @return Product
     */
    public Product findById(ObjectId id) {
        return this.productRepository.findById(id);
    }

    /**
     * Método para crear producto
     * @param product
     * @return Product
     */
    @Transactional
    public Product create(Product product) {
        product.setId(ObjectId.get());
        return this.productRepository.save(product);
    }

    /**
     * Método para actualizar producto
     * @param id
     * @param product
     */
    @Transactional
    public void update(ObjectId id, Product product) {
        product.setId(id);
        this.productRepository.save(product);
    }

    /**
     * Método para eliminar un producto
     * @param product
     */
    @Transactional
    public void delete(Product product) {
        this.productRepository.delete(product);
    }
}
