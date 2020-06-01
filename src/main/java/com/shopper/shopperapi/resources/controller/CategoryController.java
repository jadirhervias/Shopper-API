package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Category;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import com.shopper.shopperapi.services.CategoryService;
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
@RequestMapping(value = "categories", produces = "application/hal+json")
@Api(tags = "Categorías")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService ;

//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    @GetMapping
    @ApiOperation(value = "Listar categorías", notes = "Servicio para listar categorias")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Categorías listadas correctamente"),
        @ApiResponse(code = 404, message = "Categorías no encontrados")
    })
    public ResponseEntity<List<Category>> listCategories() {
        return ResponseEntity.ok(this.categoryService.findAll());
    }

    // Custom pagination for categories
    @GetMapping("{idCategory}/pagination")
    public ResponseEntity<Page<Category>> getProductsByCategoryId(@PathVariable("idCategory") ObjectId idCategory,
                                                                  @RequestParam Optional<Integer> page,
                                                                  @RequestParam Optional<String> sortBy) {

        Page<Category> categories = this.categoryService.findCategoryPagesById(
                idCategory,
                PageRequest.of(
                        page.orElse(0),
                        25,
                        Sort.Direction.ASC,
                        sortBy.orElse("id")
                )
        );

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener categoría por ID", notes = "Servicio para obtener categoría por ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Categoría encontrado"),
        @ApiResponse(code = 404, message = "Categoría no encontrado")
    })
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") ObjectId id) {
        Category category = this.categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @ApiOperation(value = "Crear categoría", notes = "Servicio para crear categoría")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Categoría creada correctamente"),
        @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        category.setId(ObjectId.get());
        Category newCategory = this.categoryService.create(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar categoría", notes = "Servicio para actualizar una categoría")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Categoría actualizada correctamente"),
        @ApiResponse(code = 404, message = "Categoría no encontrado")
    })
    public ResponseEntity<Category> updateCategory(@PathVariable("id") ObjectId id, @Valid @RequestBody Category category) {
        Category newData = this.categoryService.findById(id);
        if (newData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.categoryService.update(id, newData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar categoría", notes = "Servicio para eliminar una categoría")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Categoría eliminada correctamente"),
        @ApiResponse(code = 404, message = "Categoría no encontrada")
    })
    public void deleteCategory(@PathVariable("id") ObjectId id) {
        Category categoryToDelete = this.categoryService.findById(id);
        if (categoryToDelete != null) {
            this.categoryService.delete(categoryToDelete);
        }
    }
}
