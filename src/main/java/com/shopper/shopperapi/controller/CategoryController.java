package com.shopper.shopperapi.controller;

import com.shopper.shopperapi.model.Category;
import com.shopper.shopperapi.repository.CategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    // This annotation creates an instance of the PetsRepository object
    // that will allow us to access and modify the category database.
    private CategoryRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Category> listCategories() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Category getCategoryById(@PathVariable("id") ObjectId id) {
        return repository.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateCategory(@PathVariable("id") ObjectId id, @Valid @RequestBody Category category) {
        category.setId(id);
        repository.save(category);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Category createCategory(@Valid @RequestBody Category category) {
        category.setId(ObjectId.get());
        repository.save(category);
        return category;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCategory(@PathVariable ObjectId id) {
        repository.delete(repository.findById(id));
    }
}
