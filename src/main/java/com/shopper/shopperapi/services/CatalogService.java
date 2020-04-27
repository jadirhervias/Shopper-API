package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Catalog;
import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.repositories.CatalogRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogService {
    @Autowired
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    /**
     * Método para listar los catálogos
     * @return List<Catalog>
     */
    public List<Catalog> findAll() {
        return this.catalogRepository.findAll();
    }

    /**
     * Método para buscar catálogo por id
     * @param id
     * @return Catalog
     */
    public Catalog findById(ObjectId id) {
        return this.catalogRepository.findById(id);
    }

    /**
     * Método para obtener las categorías de un catálogo
     * @param id
     * @return Catalog
     */
//    public List<Catalog> getCatalogCategories(ObjectId id) {
//        return this.catalogRepository.getCategories(id);
//    }

    /**
     * Método para obtener los productos de un catálogo
     * @param id
     * @return Catalog
     */
//    public List<Product> getCatalogProducts(ObjectId id) {
//        return this.catalogRepository.getProducts(id);
//    }

    /**
     * Método para crear catálogo
     * @param catalog
     * @return Catalog
     */
    @Transactional
    public Catalog create(Catalog catalog) {
        catalog.setId(ObjectId.get());
        return this.catalogRepository.save(catalog);
    }

    /**
     * Método para actualizar catálogo
     * @param id
     * @param catalog
     */
    @Transactional
    public void update(ObjectId id, Catalog catalog) {
        catalog.setId(id);
        this.catalogRepository.save(catalog);
    }

    /**
     * Método para eliminar catálogo
     * @param catalog
     */
    @Transactional
    public void delete(Catalog catalog) {
        this.catalogRepository.delete(catalog);
    }
}
