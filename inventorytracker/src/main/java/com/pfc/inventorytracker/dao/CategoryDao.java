/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Category;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface CategoryDao {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    Category addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(int id);
}
