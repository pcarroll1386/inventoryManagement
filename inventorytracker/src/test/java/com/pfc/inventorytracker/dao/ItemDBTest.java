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
import static org.junit.jupiter.api.Assertions.*;
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
public class ItemDBTest {
    
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
    
    public ItemDBTest() {
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
     * Test of getAllItems method, of class ItemDB.
     */
    @Test
    public void testGetAllItems() {
    }

    /**
     * Test of addItem method, of class ItemDB.
     */
    @Test
    public void testAddItem() {
    }

    /**
     * Test of getItemById method, of class ItemDB.
     */
    @Test
    public void testGetItemById() {
    }

    /**
     * Test of updateItem method, of class ItemDB.
     */
    @Test
    public void testUpdateItem() {
    }

    /**
     * Test of deleteItem method, of class ItemDB.
     */
    @Test
    public void testDeleteItem() {
    }

    /**
     * Test of getAllItemsForLocation method, of class ItemDB.
     */
    @Test
    public void testGetAllItemsForLocation() {
    }

    /**
     * Test of getAllItemsByCategory method, of class ItemDB.
     */
    @Test
    public void testGetAllItemsByCategory() {
    }

    /**
     * Test of getAllItemsByRequest method, of class ItemDB.
     */
    @Test
    public void testGetAllItemsByRequest() {
    }
    
}
