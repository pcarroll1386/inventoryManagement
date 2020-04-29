/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.Request;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface ItemDao {

    List<Item> getAllItems();
    Item addItem(Item item);
    Item getItemById(int id);
    void updateItem(Item item);
    void deleteItem(int id);
    List<Item> getAllItemsByCategory(Category category);
}
