package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.SubCategory;
import com.shopper.shopperapi.repositories.SubCategoriyRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     *  TODO: Implementar controlador con Pageable request
     */
    /**
     * Método para retornar una lista de categorías paginadas
     */
    public Page<SubCategory> findSubCategoryPagesById(ObjectId id, Pageable pageable) {
        return subCategoryRepository.findSubCategoryPagesById(id, pageable);
    }

    /**
     * Obtener una lista de productos por categoría para paginar
     * @param id-
     * @param pageable-
     * @return List<Product>
     */
    public Page<Product> findProductsPagesById(ObjectId id, Pageable pageable) {

        List<Product> products = this.findById(id).getProducts();

        int start = (int) pageable.getOffset();

        int end = Math.min((start + pageable.getPageSize()), products.size());

        Page<Product> prodcutsPage = new PageImpl<>(products.subList(start, end), pageable, products.size());

        return prodcutsPage;
    }

    public List<Product> searchProductos(String idSubcategory, String producto) {

        Pattern searchPattern = Pattern.compile("(.*)" + producto + "(.*)", Pattern.CASE_INSENSITIVE);

        SubCategory subCategory = this.subCategoryRepository.findById(idSubcategory).get();
        return subCategory.getProducts().stream()
                .filter((item) -> searchPattern.matcher(item.getName()).find())
                .collect(Collectors.toList());
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
