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
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class LocationDBTest {
    
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
     * Test of getAllLocations method, of class LocationDB.
     */
    @Test
    public void testGetAllLocations() {
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
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        
        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        
        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3);  
        
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Location location2 = new Location();
        location2.setName("test location2 name");
        location2.setDescription("test locatin2 description");
        location2.setUser(user);
        location2.setItems(items);
        location2 = locationDao.addLocation(location2);
        
        Location location3 = new Location();
        location3.setName("test location3 name");
        location3.setDescription("test locatin3 description");
        location3.setUser(user);
        location3.setItems(items);
        location3 = locationDao.addLocation(location3);        
       
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        
        Request request2 = new Request();
        request2.setRequestDate(LocalDateTime.now());
        request2.setStatus(1);
        request2.setItems(items);
        request2.setLocationId(location2.getId());
        request2 = requestDao.addRequest(request2);
        List<Request> requests2 = new ArrayList<>();
        requests.add(request2);
        
        Request request3 = new Request();
        request3.setRequestDate(LocalDateTime.now());
        request3.setStatus(1);
        request3.setItems(items);
        request3.setLocationId(location3.getId());
        request3 = requestDao.addRequest(request3);
        List<Request> requests3 = new ArrayList<>();
        requests.add(request3);
        
        location.setRequests(requests);
        location2.setRequests(requests2);
        location3.setRequests(requests3);
        
        List<Location> fromDao = locationDao.getAllLocations();
        
        assertEquals(3, fromDao);
        assertTrue(fromDao.contains(location));
        assertTrue(fromDao.contains(location2));
        assertTrue(fromDao.contains(location3));
    }

    /**
     * Test of addLocation method & getLocationById method, of class LocationDB.
     */
    @Test
    public void testAddGetLocationById() {
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
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        
        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        
        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3);  
        
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        
        location.setRequests(requests);
        
        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    /**
     * Test of updateLocation method, of class LocationDB.
     */
    @Test
    public void testUpdateLocation() {
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
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        
        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        
        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3);  
        
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        
        location.setRequests(requests);
        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
        
        items.add(item3);
        location.setItems(items);
        location.setName("AntoherName");
        locationDao.updateLocation(location);
        
        assertNotEquals(location, fromDao);
        
        fromDao = locationDao.getLocationById(location.getId());
        
        assertEquals(location, fromDao);
    }

    /**
     * Test of deleteLocation method, of class LocationDB.
     */
    @Test
    public void testDeleteLocation() {
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
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);
        
        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        
        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3);  
        
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        
        location.setRequests(requests);
        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
        
        locationDao.deleteLocation(location.getId());
        
        fromDao = locationDao.getLocationById(location.getId());
        
        assertNull(fromDao);
    }

    /**
     * Test of getAllLocationsByUser method, of class LocationDB.
     */
    @Test
    public void testGetAllLocationsByUser() {
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
        
        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(false);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);
        
        Category category = new Category();
        category.setName("Test Category");
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
        
        Item item2 = new Item();
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        
        Item item3 = new Item();
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);
        item3 = itemDao.addItem(item3); 
        
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);
        
        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        Location location2 = new Location();
        location2.setName("test location2 name");
        location2.setDescription("test locatin2 description");
        location2.setUser(user);
        location2.setItems(items);
        location2 = locationDao.addLocation(location2);
        
        Location location3 = new Location();
        location3.setName("test location3 name");
        location3.setDescription("test locatin3 description");
        location3.setUser(user2);
        location3.setItems(items);        
        location3 = locationDao.addLocation(location3);
        
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        
        Request request2 = new Request();
        request2.setRequestDate(LocalDateTime.now());
        request2.setStatus(1);
        request2.setItems(items);
        request2.setLocationId(location2.getId());
        request2 = requestDao.addRequest(request2);
        List<Request> requests2 = new ArrayList<>();
        requests.add(request2);
        
        Request request3 = new Request();
        request3.setRequestDate(LocalDateTime.now());
        request3.setStatus(1);
        request3.setItems(items);
        request3.setLocationId(location3.getId());
        request3 = requestDao.addRequest(request3);
        List<Request> requests3 = new ArrayList<>();
        requests.add(request3);
        
        location.setRequests(requests);
        location2.setRequests(requests2);
        location3.setRequests(requests3);
        List<Location> fromDao = locationDao.getAllLocationsByUser(user);
        
        assertEquals(3, fromDao);
        assertTrue(fromDao.contains(location));
        assertTrue(fromDao.contains(location2));
        assertFalse(fromDao.contains(location3));
    }
    
}
