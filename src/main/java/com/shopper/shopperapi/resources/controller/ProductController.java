package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Image;
import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.services.ImageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import com.shopper.shopperapi.services.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
    @Autowired
    private ImageService imageService;

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
        
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
        String newProductId = ObjectId.get().toHexString();
        product.setId(newProductId);
//        product.setImage("product-" + newProductId + ".png");
        Product newProduct = this.productService.create(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    /**
     * TODO: Implementar la subida de producto con imagen con Firebase Storage
     */
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ApiOperation(value = "Crear producto", notes = "Servicio para crear productos")
//    @ApiResponses(value = {
//        @ApiResponse(code = 201, message = "Producto creado correctamente"),
//        @ApiResponse(code = 400, message = "Solicitud Inválida")
//    })
//    public ResponseEntity<Product> createProduct(
//            @RequestPart(value = "image") MultipartFile file,
//            @RequestPart(value = "product") @Valid Product product) throws IOException {
//        Image image = imageService.addImage(file);
//        String newProductId = ObjectId.get().toHexString();
//        product.setId(newProductId);
//        product.setImage(image);
//        Product newProduct = this.productService.create(product);
//        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
//    }

    @PostMapping("/image/add")
    public ResponseEntity<?> createProductWithImage(
            @RequestParam("image") MultipartFile file) throws IOException {
            imageService.addImage(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Image> getImage(@PathVariable String id) throws UnsupportedEncodingException {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar producto", notes = "Servicio para actualizar productos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Producto actualizado correctamente"),
        @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable("id") ObjectId id, @Valid @RequestBody Product productData) {
        Product productToUpdate = this.productService.findById(id);
        if (productToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.productService.update(id, productData);
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
    
    @GetMapping("/find/product/{name}")
    public ResponseEntity<?> findProductName(@PathVariable("name")String producto){
    	List<Product> products = this.productService.prodcutos(producto.replace("_", " "));
    	return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
