/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author pfcar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDBTest {
    
    @Autowired
    CategoryDao categoryDao;
    
    @Autowired
    ItemDao itemDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    RequestDao requestDao;
    
    @Autowired
    RoleDao roleDao;
    
    @Autowired
    UserDao userDao;
    
    public CategoryDBTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllCategories method, of class CategoryDB.
     */
    @Test
    public void testGetAllCategories() {
    }

    /**
     * Test of getCategoryById method, of class CategoryDB.
     */
    @Test
    public void testGetCategoryById() {
    }

    /**
     * Test of addCategory method, of class CategoryDB.
     */
    @Test
    public void testAddCategory() {
    }

    /**
     * Test of updateCategory method, of class CategoryDB.
     */
    @Test
    public void testUpdateCategory() {
    }

    /**
     * Test of deleteCategory method, of class CategoryDB.
     */
    @Test
    public void testDeleteCategory() {
    }
    
}
