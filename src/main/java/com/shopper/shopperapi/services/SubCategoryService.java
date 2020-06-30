package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.SubCategory;
import com.shopper.shopperapi.repositories.SubCategoriyRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SubCategoryService {

    private final SubCategoriyRepository subCategoryRepository;

    public SubCategoryService(SubCategoriyRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    /**
     * Método para listar categorías de productos
     * @return List<SubCategory>
     */
    public List<SubCategory> findAll() {
        return this.subCategoryRepository.findAll();
    }

    /**
     * Método para buscar categoría por id
     * @param id
     * @return Category
     */
    public SubCategory findById(ObjectId id) {
        return this.subCategoryRepository.findById(id);
    }

    /**
     * Método para retornar una lista de categorías paginadas
     */
    public Page<SubCategory> findSubCategoryPagesById(ObjectId id, Pageable pageable) {
        return subCategoryRepository.findSubCategoryPagesById(id, pageable);
    }

    /**
     * Obtener una lista de productos por categoría para paginar
     * @param id
     * @return List<?>
     */
    public List<Product> findProductsBySubCategoryId(ObjectId id) {
        List<Product> products = findById(id).getProducts();
        return products;
    }
    public List<Product> findProductsById(ObjectId id) {
        List<Product> products = findById(id).getProducts();
        return products;
    }

    /**
     * Método para crear categoría
     * @param subCategory
     * @return SubCategory
     */
    @Transactional
    public SubCategory create(SubCategory subCategory) {
        subCategory.setId(ObjectId.get().toHexString());
        return this.subCategoryRepository.save(subCategory);
    }

    /**
     * Método para actualizar categoría
     * @param oid
     * @param category
     */
    @Transactional
    /**
     * TODO: CHANGE SERVICES LIKE THIS TO TAKE STRING AS THE PARAM
     */
    public void update(ObjectId oid, SubCategory category) {
        category.setId(oid.toHexString());
        this.subCategoryRepository.save(category);
    }

    /**
     * Método para eliminar un categoría
     * @param category
     */
    @Transactional
    public void delete(SubCategory category) {
        this.subCategoryRepository.delete(category);
    }
}
