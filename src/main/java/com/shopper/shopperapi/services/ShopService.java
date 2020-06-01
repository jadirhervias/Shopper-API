package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Shop;
import com.shopper.shopperapi.repositories.ShopRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    /**
     * Método para listar los catálogos
     * @return List<Catalog>
     */
    public List<Shop> findAll() {
        return this.shopRepository.findAll();
    }

    /**
     * Método para buscar catálogo por id
     * @param id
     * @return Catalog
     */
//    public Catalog findById(String id) {
//        return this.catalogRepository.findById(id);
//    }
    public Shop findById(ObjectId id) {
        return this.shopRepository.findById(id);
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
     * @param shop
     * @return Catalog
     */
    @Transactional
    public Shop create(Shop shop) {
        shop.setId(ObjectId.get());
        return this.shopRepository.save(shop);
    }

    /**
     * Método para actualizar catálogo
     * @param id
     * @param shop
     */
    @Transactional
    public void update(ObjectId id, Shop shop) {
        shop.setId(id);
        this.shopRepository.save(shop);
    }

    /**
     * Método para eliminar catálogo
     * @param shop
     */
    @Transactional
    public void delete(Shop shop) {
        this.shopRepository.delete(shop);
    }
}
