package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Category;
import com.pfc.inventorytrackerjpa.entities.ItemType;
import com.pfc.inventorytrackerjpa.repositories.CategoryRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemTypeRepository;
import com.pfc.inventorytrackerjpa.services.InvalidCategoryException;
import com.pfc.inventorytrackerjpa.services.InvalidItemException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/itemTypes")
public class ItemTypeController {

    private final ItemTypeRepository itemTypeRepo;

    private final CategoryRepository categoryRepo;

    public ItemTypeController(ItemTypeRepository itemTypeRepo, CategoryRepository categoryRepo) {
        this.itemTypeRepo = itemTypeRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/getAll")
    public List<ItemType> getAllItemTypes() {
        return itemTypeRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public ItemType getItemTypeById(@PathVariable("id") Long id) {
        return itemTypeRepo.findById(id).orElse(null);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemType createItemType(@RequestBody ItemType itemType) throws InvalidCategoryException {
        Set<Category> validCategories = new HashSet<>();
        if (itemType.getCategories() != null) {
            for (Category c : itemType.getCategories()) {
                if (c == null) {
                    throw new InvalidCategoryException("A null category entry is not valid.");
                }
                if (c.getId() == null || c.getId() <= 0) {
                    throw new InvalidCategoryException("A valid category id is required for each category.");
                }
                Category isValid = categoryRepo.findById(c.getId()).orElse(null);
                if (isValid == null) {
                    throw new InvalidCategoryException("Category id " + c.getId() + " is not a valid category.");
                }
                validCategories.add(isValid);
            }
        }
        itemType.setCategories(validCategories);
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
        if (itemType.getCategories() != null) {
            for (Category c : itemType.getCategories()) {
                if (c == null) {
                    throw new InvalidCategoryException("A null category entry is not valid.");
                }
                if (c.getId() == null || c.getId() <= 0) {
                    throw new InvalidCategoryException("A valid category id is required for each category.");
                }
                Category isValid = categoryRepo.findById(c.getId()).orElse(null);
                if (isValid == null) {
                    throw new InvalidCategoryException("Category id " + c.getId() + " is not a valid category.");
                }
                categories.add(isValid);
            }
        }
        current.setCategories(categories);
        return itemTypeRepo.save(current);
    }

    @PostMapping("/delete/{id}")
    public boolean deleteItemTypeById(@PathVariable("id") Long id) {
        ItemType itemType = itemTypeRepo.findById(id).orElse(null);
        if (itemType != null) {
            itemTypeRepo.delete(itemType);
            return true;
        }
        return false;
    }
}
