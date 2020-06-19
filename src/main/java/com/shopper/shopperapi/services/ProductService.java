package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.SubCategory;
import com.shopper.shopperapi.repositories.ImageRepository;
import com.shopper.shopperapi.repositories.ProductRepository;
import com.shopper.shopperapi.repositories.SubCategoriyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubCategoriyRepository subCategoriyRepository;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Método para listar productos
     * @return List<Product>
     */
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    /**
     * Método para retornar una lista de productos paginados de cierta categoría
     */
    public List<Product> getSubCategoryProducts(ObjectId idCategory) {
        SubCategory subCategory = this.subCategoriyRepository.findById(idCategory);
        return subCategory.getProducts();
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
