package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Catalog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.shopper.shopperapi.services.CatalogService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
@Api(tags = "Catálogos")
@CrossOrigin(origins = "*")
public class CatalogController {
    @Autowired
    // This annotation creates an instance of the PetsRepository object
    // that will allow us to access and modify the catalog database.
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @ApiOperation(value = "Listar catálogos", notes = "Servicio para listar catálogos")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Catálogo listados correctamente"),
        @ApiResponse(code = 404, message = "Catálogos no encontrados")
    })
    public ResponseEntity<List<Catalog>> listCatalogs() {
        return ResponseEntity.ok(catalogService.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener catálogo por ID", notes = "Servicio para obtener catálogo por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Catálogo encontrado"),
            @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public ResponseEntity<Catalog> getCatalogById(@PathVariable("id") ObjectId id) {
        Catalog catalog = this.catalogService.findById(id);
        return ResponseEntity.ok(catalog);
    }

//    @GetMapping("/{id}/categories")
//    public ResponseEntity<List<Catalog>> listCatalogCategories(@PathVariable("id") ObjectId id) {
//        return ResponseEntity.ok(this.catalogService.getCatalogCategories(id));
//    }

//    @GetMapping("/{id}/products")
//    public ResponseEntity<List<Product>> listCatalogProducts(@PathVariable("id") ObjectId id) {
//        return ResponseEntity.ok(this.catalogService.getCatalogProducts(id));
//    }

    @PostMapping
    @ApiOperation(value = "Crear catálogo", notes = "Servicio para crear catálogos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo creado correctamente"),
        @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<Catalog> createCatalog(@Valid @RequestBody Catalog catalog) {
        catalog.setId(ObjectId.get());
        Catalog newCatalog = this.catalogService.create(catalog);
        return new ResponseEntity<>(newCatalog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar catálogo", notes = "Servicio para actualizar un catálogo")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo actualizado correctamente"),
        @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public ResponseEntity<Catalog> updateCatalog(@PathVariable("id") ObjectId id, @Valid @RequestBody Catalog catalog) {
        Catalog newData = this.catalogService.findById(id);
        if (newData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.catalogService.update(id, newData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar catálogo", notes = "Servicio para eliminar un catálogo")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo eliminado correctamente"),
        @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public void deleteCatalog(@PathVariable("id") ObjectId id) {
        Catalog catalogToDelete = this.catalogService.findById(id);
        if (catalogToDelete != null) {
            this.catalogService.delete(catalogToDelete);
        }
    }
}
