package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.ResponseShopsOrder;
import com.shopper.shopperapi.models.Shop;
import com.shopper.shopperapi.repositories.ShopRepository;
import com.shopper.shopperapi.utils.distance.DistanceCalculated;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;
//    private DistanceCalculated distanceCalculated;

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
//        shop.setId(ObjectId.get());
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

    public List<ResponseShopsOrder> getNearestShopsForUser(Double userLat, Double userLng, String id){

        List<ResponseShopsOrder> orderedShops = new ArrayList<ResponseShopsOrder>();

            // Ordenar tiendas por distancia al usuario
    		if(id == null) {

            	List<Shop> shops = shopRepository.findAll();
                for (Shop shop : shops) {
                    double distance = DistanceCalculated.distanceCoord(
                            userLat, userLng, shop.getShopLat(), shop.getShopLng()
                    );
                    shop.setCategories(null);
                    orderedShops.add(new ResponseShopsOrder(distance, shop));
                }

            // Mostrar distancia de la tienda al usuario
            } else {

            	Shop shop = shopRepository.findById(new ObjectId(id));
//            	double distancia = distanceCalculated.distanceCoord(

                /**
                 * TODO: Validar si la tienda no se encuentra (Muestra ERROR 500!!!!)
                 */
                double distance = DistanceCalculated.distanceCoord(
                        userLat, userLng, shop.getShopLat(), shop.getShopLng()
                );

            	shop.setCategories(null);
            	orderedShops.add(new ResponseShopsOrder(distance, shop));

            }

    		return orderedShops;
    }
}
