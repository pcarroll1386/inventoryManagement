package com.pfc.inventorytracker.dao;

import java.util.List;

import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.LocationItem;

public interface LocationItemDao {

    List<LocationItem> getAllLocationItems();
    LocationItem addLocationItem(LocationItem locationItem);
    LocationItem getLocationItemyId(String id);
    void updateLocationItem(LocationItem item);
    void deleteLocationItemById(String id);
    List<LocationItem> getAllLocationItemsByCategory(Category category);
    
}
