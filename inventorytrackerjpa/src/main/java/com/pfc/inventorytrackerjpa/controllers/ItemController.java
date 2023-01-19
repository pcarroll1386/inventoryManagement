package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Category;
import com.pfc.inventorytrackerjpa.entities.Item;
import com.pfc.inventorytrackerjpa.repositories.CategoryRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemRepository;
import com.pfc.inventorytrackerjpa.service.InvalidCategoryException;
import com.pfc.inventorytrackerjpa.service.InvalidItemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping("/getAll")
    public List<Item> getAllItems() {return itemRepo.findAll();}

    @GetMapping("/getById/{id}")
    public Item getItemById(@PathVariable("id") UUID id){
        Item item =  itemRepo.findById(id).orElse(null);
        return item;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestBody Item item) throws InvalidCategoryException{
        for(Category c: item.getCategories()){
            Category isValid = categoryRepo.findById(c.getId()).orElse(null);
            if(isValid == null){
                throw new InvalidCategoryException(c.getName() + " is not a valid category.");
            }
        }
        Item savedItem = itemRepo.save(item);
        return savedItem;
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item editItem(@RequestBody Item item) throws InvalidItemException, InvalidCategoryException {
        Item currentItem = itemRepo.findById(item.getId()).orElse(null);
        if(currentItem == null){
            throw new InvalidItemException("Please provide a valid item");
        }
        currentItem.setName(item.getName());
        currentItem.setModelNumber(item.getModelNumber());
        Set<Category> categories = new HashSet<>();
        for(Category c: item.getCategories()){
            Category isValid = categoryRepo.findById(c.getId()).orElse(null);
            if(isValid == null){
                throw new InvalidCategoryException(c.getName() + " is not a valid category");
            }
            categories.add(c);
        }
        currentItem.setCategories(categories);
        Item updatedItem = itemRepo.save(currentItem);
        return updatedItem;
    }

    @PostMapping("delete/{id}")
    public boolean deleteItemById(@PathVariable("id") UUID id){
        Item item = itemRepo.findById(id).orElse(null);
        if(item != null){
            itemRepo.delete(item);
            return true;
        }
        return false;
    }

}
