package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import com.shopper.shopperapi.services.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa el servicio web de Productos
 * @author jadir
 *
 */
@RestController
@RequestMapping(value = "products", produces = "application/hal+json")
@Api(tags = "Productos")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ApiOperation(value = "Listar productos", notes = "Servicio para listar productos")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Productos listados correctamente"),
        @ApiResponse(code = 404, message = "Producto no encontrados")
    })
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(this.productService.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener producto por ID", notes = "Servicio para obtener producto por ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Producto encontrado"),
        @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public ResponseEntity<Product> getProductById(@PathVariable("id") ObjectId id) {
        Product product = this.productService.findById(id);
        return ResponseEntity.ok(product);
    }

    //     Custom pagination for products
//    @GetMapping("category/{idCategory}")
//    public ResponseEntity<Page<Product>> getProductsByCategoryId(@PathVariable("idCategory") ObjectId idCategory,
//                                                           @RequestParam Optional<Integer> page,
//                                                           @RequestParam Optional<String> sortBy) {
//
//        Page<Product> productPagination = this.productService.getProductsPagesByCategoryId(idCategory, page, sortBy);
//
//        return ResponseEntity.ok(productPagination);
//    }

    @PostMapping
    @ApiOperation(value = "Crear producto", notes = "Servicio para crear productos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Producto creado correctamente"),
        @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        product.setId(ObjectId.get());
        Product newProduct = this.productService.create(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar producto", notes = "Servicio para actualizar productos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Producto actualizado correctamente"),
        @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable("id") ObjectId id, @Valid @RequestBody Product product) {
        Product newData = this.productService.findById(id);
        if (newData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.productService.update(id, newData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar producto", notes = "Servicio para eliminar productos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Producto eliminado correctamente"),
        @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public void deleteProduct(@PathVariable("id") ObjectId id) {
        Product productToDelete = this.productService.findById(id);
        if (productToDelete != null) {
            this.productService.delete(productToDelete);
        }
    }
}
