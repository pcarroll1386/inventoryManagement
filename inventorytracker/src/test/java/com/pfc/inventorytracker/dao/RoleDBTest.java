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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class RoleDBTest {
    
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
    
    public RoleDBTest() {
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
     * Test of getAllRoles method, of class RoleDB.
     */
    @Test
    public void testGetAllRoles() {
        Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);
        
        Role role2 = new Role();
        role2.setRole("ROLE_TEST2");
        role2 = roleDao.addRole(role2);
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(role2);
        
        User user = new User();
        user.setUsername("test username");
        user.setPassword("test password");
        user.setEnabled(true);
        user.setRoles(roles);        
        user = userDao.addUser(user);
        
        List<Role> fromDao = roleDao.getAllRoles();
        
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(role));
        assertTrue(fromDao.contains(role2));
        
    }

    /**
     * Test of addRole method & getRoleById method, of class RoleDB.
     */
    @Test
    public void testAddGetRoleById() {
    }

    /**
     * Test of updateRole method, of class RoleDB.
     */
    @Test
    public void testUpdateRole() {
    }

    /**
     * Test of deleteRole method, of class RoleDB.
     */
    @Test
    public void testDeleteRole() {
    }
    
}