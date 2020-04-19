package com.shopper.shopperapi.controller;

import com.shopper.shopperapi.model.Product;
import com.shopper.shopperapi.repository.ProductRepository;
//import com.shopper.shopperapi.service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Clase que representa el servicio web de Reserva
 * @author jadir
 *
 */
@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

//    private final ProductService productService;

//    public ReservaResource(ProductService productService) {
//        this.productService = productService;
//    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Product> listProducts() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable("id") ObjectId id) {
        return repository.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateProductById(@PathVariable("id") ObjectId id, @Valid @RequestBody Product product) {
        product.setId(id);
        repository.save(product);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Product createProduct(@Valid @RequestBody Product product) {
        product.setId(ObjectId.get());
        repository.save(product);
        return product;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable ObjectId id) {
        repository.delete(repository.findById(id));
    }
}
