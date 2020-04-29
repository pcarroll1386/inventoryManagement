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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
        List<Role> roles = roleDao.getAllRoles();
        for (Role role : roles) {
            roleDao.deleteRole(role.getId());
        }
        List<Category> categories = categoryDao.getAllCategories();
        for (Category category : categories) {
            categoryDao.deleteCategory(category.getId());
        }
        List<Request> requests = requestDao.getAllRequests();
        for (Request request : requests) {
            requestDao.deleteRequest(request.getId());
        }
        List<Item> items = itemDao.getAllItems();
        for (Item item : items) {
            itemDao.deleteItem(item.getId());
        }
        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocation(location.getId());
        }
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            userDao.deleteUser(user.getUsername());
        }
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllCategories method, of class CategoryDB.
     */
    @Test
    public void testGetAllCategories() {
        Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user = userDao.addUser(user);

        Category category = new Category();
        category.setName("test name");
        category = categoryDao.addCategory(category);

        Category category2 = new Category();
        category2.setName("Test name 2");
        category2 = categoryDao.addCategory(category2);

        Category category3 = new Category();
        category3.setName("Test name 3");
        category3 = categoryDao.addCategory(category3);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);
        categories.add(category3);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);

        List<Category> fromDao = categoryDao.getAllCategories();

        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(category));
        assertTrue(fromDao.contains(category2));
        assertTrue(fromDao.contains(category3));

    }

    /**
     * Test of addCategory method & getCategoryById method, of class CategoryDB.
     */
    @Test
    public void testAddGetCategoryById() {
        Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user = userDao.addUser(user);

        Category category = new Category();
        category.setName("Test name");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);

        Category fromDao = categoryDao.getCategoryById(category.getId());

        assertEquals(category, fromDao);

    }

    /**
     * Test of updateCategory method, of class CategoryDB.
     */
    @Test
    public void testUpdateCategory() {
        Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user = userDao.addUser(user);

        Category category = new Category();
        category.setName("Test name");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);

        Category fromDao = categoryDao.getCategoryById(category.getId());
        assertEquals(category, fromDao);

        category.setName("Antoher test Name");
        categoryDao.updateCategory(category);

        assertNotEquals(category, fromDao);

        fromDao = categoryDao.getCategoryById(category.getId());

        assertEquals(category, fromDao);
    }

    /**
     * Test of deleteCategory method, of class CategoryDB.
     */
    @Test
    public void testDeleteCategory() {
         Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user = userDao.addUser(user);
        
        Category category = new Category();
        category.setName("Test name");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        
        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        List<Item> items = new ArrayList<>();
        items.add(item);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Category fromDao = categoryDao.getCategoryById(category.getId());        
        assertEquals(category, fromDao);
        
        categoryDao.deleteCategory(category.getId());
        fromDao = categoryDao.getCategoryById(category.getId());
        
        assertNull(fromDao);
        
    }

}
