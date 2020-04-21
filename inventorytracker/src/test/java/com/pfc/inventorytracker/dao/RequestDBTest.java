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
import com.pfc.inventorytracker.entities.Role;
import com.pfc.inventorytracker.entities.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
public class RequestDBTest {
    
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
    
    public RequestDBTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Role> roles = roleDao.getAllRoles();
        for(Role role : roles){
            roleDao.deleteRole(role.getId());
        }
        List<Category> categories = categoryDao.getAllCategories();
        for(Category category : categories){
            categoryDao.deleteCategory(category.getId());
        }
        List<Request> requests = requestDao.getAllRequests();
        for(Request request : requests){
            requestDao.delteRequest(request.getId());
        }
        List<Item> items = itemDao.getAllItems();
        for(Item item : items){
            itemDao.deleteItem(item.getId());
        }
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations){
            locationDao.deleteLocation(location.getId());
        }
        List<User> users = userDao.getAllUsers();
        for(User user : users){
            userDao.deleteUser(user.getUsername());
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllRequests method, of class RequestDB.
     */
    @Test
    public void testGetAllRequests() {
    }

    /**
     * Test of getRequestById method, of class RequestDB.
     */
    @Test
    public void testGetRequestById() {
    }

    /**
     * Test of addRequest method, of class RequestDB.
     */
    @Test
    public void testAddRequest() {
    }

    /**
     * Test of updateRequest method, of class RequestDB.
     */
    @Test
    public void testUpdateRequest() {
    }

    /**
     * Test of delteRequest method, of class RequestDB.
     */
    @Test
    public void testDelteRequest() {
    }

    /**
     * Test of getAllRequestsByLocation method, of class RequestDB.
     */
    @Test
    public void testGetAllRequestsByLocation() {
    }

    /**
     * Test of getAllRequestsByUser method, of class RequestDB.
     */
    @Test
    public void testGetAllRequestsByUser() {
    }
    
}
