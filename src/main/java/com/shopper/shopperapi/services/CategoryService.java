package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.repositories.CategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    public CategoryService(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }

    /**
     * Método para listar categorías de productos
     * @return List<Category>
     */
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    /**
     * Método para buscar categoría por id
     * @param id
     * @return Category
     */
    public Category findById(ObjectId id) {
        return this.categoryRepository.findById(id);
    }

    /**
     * Método para retornar una lista de categorías paginadas
     */
    public Page<Category> findCategoryPagesById(ObjectId id, Pageable pageable) {
        return categoryRepository.findCategoryPagesById(id, pageable);
    }

    /**
     * Método para crear categoría
     * @param category
     * @return Category
     */
    @Transactional
    public Category create(Category category) {
        category.setId(ObjectId.get().toHexString());
        return this.categoryRepository.save(category);
    }

    /**
     * Método para actualizar categoría
     * @param oid
     * @param category
     */
    @Transactional
    public void update(ObjectId oid, Category category) {
        category.setId(oid.toHexString());
        this.categoryRepository.save(category);
    }

    /**
     * Método para eliminar un categoría
     * @param category
     */
    @Transactional
    public void delete(Category category) {
        this.categoryRepository.delete(category);
    }
}
