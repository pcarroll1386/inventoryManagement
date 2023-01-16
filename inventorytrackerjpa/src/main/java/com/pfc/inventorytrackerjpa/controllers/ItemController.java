package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Item;
import com.pfc.inventorytrackerjpa.repositories.CategoryRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemRepository itemRepo;

    @GetMapping("/getAll")
    public List<Item> getAllItems() {return itemRepo.findAll();}

}
