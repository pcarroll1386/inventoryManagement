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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<Category> categories = categoryDao.getAllCategories();
        for(Category category : categories){
            categoryDao.deleteCategory(category.getId());
        }
        List<Item> items = itemDao.getAllItems();
        for(Item item : items){
            itemDao.deleteItem(item.getId());
        }
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations){
            locationDao.deleteLocation(location.getId());
        }
        List<Request> requests = requestDao.getAllRequests();
        for(Request request : requests){
            requestDao.deleteRequest(request.getId());
        }
        List<Role> roles = roleDao.getAllRoles();
        for(Role role : roles){
            roleDao.deleteRole(role.getId());
        }
        List<User> users = userDao.getAllUsers();
        for(User user : users){
            userDao.deleteUser(user.getUsername());
        }
    }
    
    @After
    public void tearDown() {
    }

        /**
     * Test of getAllItems method, of class ItemDB.
     */
    @Test
    public void testGetAllItems() {

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
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3);

        List<Item> fromDao = itemDao.getAllItems();

        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(item));
        assertTrue(fromDao.contains(item2));
        assertTrue(fromDao.contains(item3));

    }

    /**
     * Test of getItemById method & addItem method, of class ItemDB.
     */
    @Test
    public void testAddGetItem() {
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
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item fromDao = itemDao.getItemById(item.getId());

        assertEquals(item, fromDao);
    }

    /**
     * Test of updateItem method, of class ItemDB.
     */
    @Test
    public void testUpdateItem() {
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
        category.setName("Test Category");
        category = categoryDao.addCategory(category);

        Category category2 = new Category();
        category2.setName("Test Category2");
        category2 = categoryDao.addCategory(category2);

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
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

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setLocationId(location.getId());
        request.setItems(items);        
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);

        location.setRequests(requests);
        
        Item fromDao = itemDao.getItemById(item.getId());
        assertEquals(item, fromDao);

        categories.add(category2);
        item.setCategories(categories);
        item.setName("another test name");
        itemDao.updateItem(item);

        assertNotEquals(item, fromDao);

        fromDao = itemDao.getItemById(item.getId());

        assertEquals(item, fromDao);

    }

    /**
     * Test of deleteItem method, of class ItemDB.
     */
    @Test
    public void testDeleteItem() {
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
        category.setName("Test Category");
        category = categoryDao.addCategory(category);

        Category category2 = new Category();
        category2.setName("Test Category2");
        category2 = categoryDao.addCategory(category2);

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
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

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setLocationId(location.getId());
        request.setItems(items);
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);

        location.setRequests(requests);
        
        Item fromDao = itemDao.getItemById(item.getId());
        assertEquals(item, fromDao);

        itemDao.deleteItem(item.getId());

        fromDao = itemDao.getItemById(item.getId());

        assertNull(fromDao);
    }

    /**
     * Test of getAllItemsByCategory method, of class ItemDB.
     */
    @Test
    public void testGetAllItemsByCategory() {
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
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Category category2 = new Category();
        category2.setName("test category2 name");
        category2 = categoryDao.addCategory(category2);
        Set<Category> categories2 = new HashSet<>();
        categories2.add(category2);

        Item item = new Item();
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories2);
        item3 = itemDao.addItem(item3);
        
        List<Item> fromDao = itemDao.getAllItemsByCategory(category);

        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(item));
        assertTrue(fromDao.contains(item2));
        assertFalse(fromDao.contains(item3));
    }
    
}
