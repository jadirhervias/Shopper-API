package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.repositories.CategoryRepository;
import com.shopper.shopperapi.repositories.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;


//    @Autowired
//    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
//        this.productRepository = productRepository;
//        this.categoryService = categoryService;
//    }

    /**
     * Método para listar usuarios
     * @return List<Product>
     */
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    /**
     * Método para retornar una lista de productos paginados de cierta categoría
     */
    public List<Product> getCategoryProducts(ObjectId idCategory) {
        Category category = this.categoryRepository.findById(idCategory);
        return category.getProducts();
    }

//    public Page<Product> getProductsPagesByCategoryId (ObjectId id, Optional<Integer> page,
//                                                       Optional<String> sortBy) {
//        Page<Product> productPagination = this.productRepository.findProductPages(
//                this.getCategoryProducts(id),
//                PageRequest.of(
//                        page.orElse(0),
//                        25,
//                        Sort.Direction.ASC,
//                        sortBy.orElse("id")
//                )
//        );
//
//        return productPagination;
//    }

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
