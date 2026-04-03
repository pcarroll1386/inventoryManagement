package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.Item;
import com.pfc.inventorytrackerjpa.entities.ItemType;
import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.repositories.ItemRepository;
import com.pfc.inventorytrackerjpa.repositories.ItemTypeRepository;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidItemException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepo;

    private final ItemTypeRepository itemTypeRepo;

    private final LocationRepository locationRepo;

    public ItemController(ItemRepository itemRepo, ItemTypeRepository itemTypeRepo, LocationRepository locationRepo) {
        this.itemRepo = itemRepo;
        this.itemTypeRepo = itemTypeRepo;
        this.locationRepo = locationRepo;
    }

    @GetMapping("/getAll")
    public List<Item> getAll() {
        return itemRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public Item getById(@PathVariable("id") long id) {
        return itemRepo.findById(id).orElse(null);
    }

    @GetMapping("/getByLocation/{locationId}")
    public List<Item> getByLocation(@PathVariable("locationId") long locationId) {
        return itemRepo.findByLocationId(locationId);
    }

    @GetMapping("/getByItemType/{itemTypeId}")
    public List<Item> getByItemType(@PathVariable("itemTypeId") String itemTypeId) {
        return itemRepo.findByItemTypeId(itemTypeId);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestBody Item item) throws InvalidDataException {
        Location location = locationRepo.findById(item.getLocation().getId()).orElse(null);
        if (location == null) {
            throw new InvalidDataException("Please provide a valid location.");
        }
        ItemType itemType = itemTypeRepo.findById(item.getItemType().getId()).orElse(null);
        if (itemType == null) {
            throw new InvalidDataException("Please provide a valid item type.");
        }
        item.setLocation(location);
        item.setItemType(itemType);
        return itemRepo.save(item);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item editItem(@RequestBody Item item) throws InvalidItemException, InvalidDataException {
        Item current = itemRepo.findById(item.getId()).orElse(null);
        if (current == null) {
            throw new InvalidItemException("Please provide a valid item.");
        }
        Location location = locationRepo.findById(item.getLocation().getId()).orElse(null);
        if (location == null) {
            throw new InvalidDataException("Please provide a valid location.");
        }
        ItemType itemType = itemTypeRepo.findById(item.getItemType().getId()).orElse(null);
        if (itemType == null) {
            throw new InvalidDataException("Please provide a valid item type.");
        }
        current.setLocation(location);
        current.setItemType(itemType);
        current.setSerialNumber(item.getSerialNumber());
        current.setPrice(item.getPrice());
        current.setMax(item.getMax());
        current.setMin(item.getMin());
        return itemRepo.save(current);
    }

    @PostMapping("/delete/{id}")
    public boolean deleteItemById(@PathVariable("id") long id) {
        Item item = itemRepo.findById(id).orElse(null);
        if (item != null) {
            itemRepo.delete(item);
            return true;
        }
        return false;
    }
}
