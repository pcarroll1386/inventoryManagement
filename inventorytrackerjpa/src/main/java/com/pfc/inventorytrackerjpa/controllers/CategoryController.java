package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Category;
import com.pfc.inventorytrackerjpa.repositories.CategoryRepository;
import com.pfc.inventorytrackerjpa.services.InvalidCategoryException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepo;

    public CategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/getAll")
    public List<Category> getAll(){return categoryRepo.findAll(); }

    @GetMapping("/getById/{id}")
    public Category getCategoryById(@PathVariable("id") Long id){
        Category category = categoryRepo.findById(id).orElse(null);
        return category;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category){
        Category savedCategory = categoryRepo.save(category);
        return savedCategory;
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category editCategory(@RequestBody Category category) throws InvalidCategoryException{
        Category currentCategory = categoryRepo.findById(category.getId()).orElse(null);
        if(currentCategory == null){

            throw new InvalidCategoryException("Please provide valid Category to update");
        }
        currentCategory.setName(category.getName());
        Category updatedCategory = categoryRepo.save(currentCategory);
        return updatedCategory;
    }

    @PostMapping("/delete/{id}")
    public boolean deleteCategoryById(@PathVariable("id") Long id){
        Category category = categoryRepo.findById(id).orElse(null);
        if(category != null){
            categoryRepo.delete(category);
            return true;
        }
        return false;
    }
}
