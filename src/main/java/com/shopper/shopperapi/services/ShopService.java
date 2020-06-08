package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.ResponseShopsOrder;
import com.shopper.shopperapi.models.Shop;
import com.shopper.shopperapi.repositories.ShopRepository;
import com.shopper.shopperapi.utils.distance.DistanceCalculated;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;
    private DistanceCalculated distanceCalculated;

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

    public List<ResponseShopsOrder> OrdenarTiendas(Double user_lat,Double user_lng,ObjectId id){
    	List<ResponseShopsOrder> ordenar=new ArrayList<ResponseShopsOrder>();
    		if(id == null) {
            	List<Shop> tiendas = shopRepository.findAll();
                for (int i=0; i<tiendas.size();i++){
                    double distancia = distanceCalculated.distanceCoord(user_lat,user_lng,tiendas.get(i).getShop_lat(),tiendas.get(i).getShop_lng());
                    tiendas.get(i).setCategories(null);
                    ordenar.add(new ResponseShopsOrder(distancia, tiendas.get(i)));
                }
            }else {
            	Shop shop = shopRepository.findById(id);
            	double distancia = distanceCalculated.distanceCoord(user_lat,user_lng,shop.getShop_lat(),shop.getShop_lng());
            	shop.setCategories(null);
            	ordenar.add(new ResponseShopsOrder(distancia, shop));
            }
        return ordenar;
    }
}
