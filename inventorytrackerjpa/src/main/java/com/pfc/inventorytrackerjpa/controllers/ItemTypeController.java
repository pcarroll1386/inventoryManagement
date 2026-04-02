package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Category;
import com.pfc.inventorytrackerjpa.entities.ItemType;
import com.pfc.inventorytrackerjpa.repositories.CategoryRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemTypeRepository;
import com.pfc.inventorytrackerjpa.services.InvalidCategoryException;
import com.pfc.inventorytrackerjpa.services.InvalidItemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/itemTypes")
public class ItemTypeController {

    @Autowired
    ItemTypeRepository itemTypeRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping("/getAll")
    public List<ItemType> getAllItemTypes() {
        return itemTypeRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public ItemType getItemTypeById(@PathVariable("id") String id) {
        return itemTypeRepo.findById(id).orElse(null);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemType createItemType(@RequestBody ItemType itemType) throws InvalidCategoryException {
        for (Category c : itemType.getCategories()) {
            Category isValid = categoryRepo.findById(c.getId()).orElse(null);
            if (isValid == null) {
                throw new InvalidCategoryException(c.getName() + " is not a valid category.");
            }
        }
        return itemTypeRepo.save(itemType);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ItemType editItemType(@RequestBody ItemType itemType) throws InvalidItemException, InvalidCategoryException {
        ItemType current = itemTypeRepo.findById(itemType.getId()).orElse(null);
        if (current == null) {
            throw new InvalidItemException("Please provide a valid item type.");
        }
        current.setName(itemType.getName());
        current.setNickname(itemType.getNickname());
        current.setDescription(itemType.getDescription());
        Set<Category> categories = new HashSet<>();
        for (Category c : itemType.getCategories()) {
            Category isValid = categoryRepo.findById(c.getId()).orElse(null);
            if (isValid == null) {
                throw new InvalidCategoryException(c.getName() + " is not a valid category.");
            }
            categories.add(c);
        }
        current.setCategories(categories);
        return itemTypeRepo.save(current);
    }

    @PostMapping("/delete/{id}")
    public boolean deleteItemTypeById(@PathVariable("id") String id) {
        ItemType itemType = itemTypeRepo.findById(id).orElse(null);
        if (itemType != null) {
            itemTypeRepo.delete(itemType);
            return true;
        }
        return false;
    }
}
