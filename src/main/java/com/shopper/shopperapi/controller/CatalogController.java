package com.shopper.shopperapi.controller;

import com.shopper.shopperapi.model.Catalog;
import com.shopper.shopperapi.model.Category;
import com.shopper.shopperapi.model.Product;
import com.shopper.shopperapi.repository.CatalogRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    // This annotation creates an instance of the PetsRepository object
    // that will allow us to access and modify the catalog database.
    private CatalogRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Catalog> listCatalogs() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Catalog getCatalogById(@PathVariable("id") ObjectId id) {
        return repository.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateCatalogById(@PathVariable("id") ObjectId id, @Valid @RequestBody Catalog catalog) {
        catalog.setId(id);
        repository.save(catalog);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Catalog createCatalog(@Valid @RequestBody Catalog catalog) {
        catalog.setId(ObjectId.get());
        repository.save(catalog);
        return catalog;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCatalog(@PathVariable ObjectId id) {
        repository.delete(repository.findById(id));
    }

    /*
        Método para obtener todos los productos de un catálogo
    */
//    @RequestMapping(value = "{id}/products", method = RequestMethod.GET)
//    public List<Product> listCatalogProducts(@PathVariable ObjectId id) {
////        ArrayList<Product> products = new ArrayList();
////        ArrayList<Category> categories = repository.listCategories(id);
//        return repository.listCatalogProductsById(id);
//    }
//
//    @RequestMapping(value = "{idCatalog}/{idCategory}/products", method = RequestMethod.GET)
//    public List<Product> getCategoryProducts(@PathVariable ObjectId idCatalog, @PathVariable ObjectId idCategory) {
//        return repository.getCategoryProductsById(idCatalog, idCategory);
//    }
}
