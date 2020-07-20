package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.SubCategory;
import com.shopper.shopperapi.services.SubCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@RestController
@RequestMapping(value = "subcategories", produces = "application/hal+json")
@Api(tags = "Sub Categorías")
@CrossOrigin(origins = "*")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    @ApiOperation(value = "Listar sub-categorías", notes = "Servicio para listar sub-categorias")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sub Categorías listadas correctamente"),
            @ApiResponse(code = 404, message = "Sub Categorías no encontrados")
    })
    public ResponseEntity<List<SubCategory>> listSubCategories() {
        return ResponseEntity.ok(this.subCategoryService.findAll());
    }

    // Custom products pagination for sub categories
    @GetMapping("{idSubCategory}/pagination")
    public ResponseEntity<Page<Product>> getProductsBySubCategoryId(@PathVariable("idSubCategory") String idSubCategory,
                                                                  @RequestParam Optional<Integer> page,
                                                                  @RequestParam Optional<String> sortBy) {

        Page<Product> productPages = this.subCategoryService.findProductsPagesById(
                new ObjectId(idSubCategory),
                PageRequest.of(
                        page.orElse(0),
                        25,
                        Sort.Direction.ASC,
                        sortBy.orElse("id")
                )
        );

        return ResponseEntity.ok(productPages);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener sub-categoría por ID", notes = "Servicio para obtener sub-categoría por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sub-Categoría encontrado"),
            @ApiResponse(code = 404, message = "Sub-Categoría no encontrado")
    })
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable("id") ObjectId id) {
        SubCategory subCategory = this.subCategoryService.findById(id);

        if (subCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(subCategory);
    }

    @PostMapping
    @ApiOperation(value = "Crear sub-categoría", notes = "Servicio para crear sub categoría")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sub Categoría creada correctamente"),
            @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<SubCategory> createSubCategory(@Valid @RequestBody SubCategory subCategory) {
        subCategory.setId(ObjectId.get().toHexString());
        SubCategory newSubCategory = this.subCategoryService.create(subCategory);
        return new ResponseEntity<>(newSubCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar sub-categoría", notes = "Servicio para actualizar una sub-categoría")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sub-Categoría actualizada correctamente"),
            @ApiResponse(code = 404, message = "Sub-Categoría no encontrado")
    })
    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable("id") ObjectId id, @Valid @RequestBody SubCategory subCategoryData) {
        SubCategory subCategoryToUpdate = this.subCategoryService.findById(id);
        if (subCategoryToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.subCategoryService.update(id, subCategoryData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar sub-categoría", notes = "Servicio para eliminar una sub categoría")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sub Categoría eliminada correctamente"),
            @ApiResponse(code = 404, message = "Sub Categoría no encontrada")
    })
    public void deleteSubCategory(@PathVariable("id") ObjectId id) {
        SubCategory subCategoryToDelete = this.subCategoryService.findById(id);
        if (subCategoryToDelete != null) {
            this.subCategoryService.delete(subCategoryToDelete);
        }
    }
}
