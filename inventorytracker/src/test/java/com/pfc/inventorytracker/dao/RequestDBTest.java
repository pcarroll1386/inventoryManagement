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

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<Category> categories = categoryDao.getAllCategories();
        for (Category category : categories) {
            categoryDao.deleteCategory(category.getId());
        }
        List<Item> items = itemDao.getAllItems();
        for (Item item : items) {
            itemDao.deleteItemById(item.getId());
        }
        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocation(location.getId());
        }
        List<Request> requests = requestDao.getAllRequests();
        for (Request request : requests) {
            requestDao.deleteRequest(request.getId());
        }
        List<Role> roles = roleDao.getAllRoles();
        for (Role role : roles) {
            roleDao.deleteRole(role.getId());
        }
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            userDao.deleteUser(user.getUsername());
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllRequest method, of class RequestDB.
     */
    @Test
    public void testGetAllRequests() {
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
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
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

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        items = new ArrayList<>();
        items.add(item);

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setLocationId(location.getId());
        request.setItems(items);
        request = requestDao.addRequest(request);

        Request request2 = new Request();
        request2.setRequestDate(LocalDateTime.now().withNano(0));
        request2.setStatus(1);
        request2.setItems(items);
        request2.setLocationId(location.getId());
        request2 = requestDao.addRequest(request2);

        Request request3 = new Request();
        request3.setRequestDate(LocalDateTime.now().withNano(0));
        request3.setStatus(1);
        request3.setLocationId(location.getId());
        request3.setItems(items);
        request3 = requestDao.addRequest(request3);

        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);
        requests.add(request3);

        location.setRequests(requests);
        List<Request> fromDao = requestDao.getAllRequests();

        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(request));
        assertTrue(fromDao.contains(request2));
        assertTrue(fromDao.contains(request3));
    }

    /**
     * Test of addRequest Method and getRequestById method, of class RequestDB.
     */
    @Test
    public void testAddGetRequest() {

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
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setInInventory(3);
        item2.setMax(10);
        item2.setMin(5);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test locatin description");
        location.setUser(user);
        location.setItems(items);
        location = locationDao.addLocation(location);

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);

        item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);

        items = new ArrayList<>();
        items.add(item);
        items.add(item2);

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);

        location.setRequests(requests);

        Request fromDao = requestDao.getRequestById(request.getId());

        assertEquals(request, fromDao);
    }

    /**
     * Test of updateRequest method, of class RequestDB.
     */
    @Test
    public void testUpdateRequest() {
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
        user2.setUsername("testUsername2");
        user2.setPassword("testPassword2");
        user2.setEnabled(true);
        user2.setRoles(roles);
        userDao.addUser(user2);

        Category category = new Category();
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
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

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        items = new ArrayList<>();
        items.add(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setQuantity(7);
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);
        List<Item> newItems = new ArrayList<>();
        newItems.add(item);
        newItems.add(item2);

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setItems(items);
        request.setLocationId(location.getId());
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);

        location.setRequests(requests);
        Request fromDao = requestDao.getRequestById(request.getId());
        assertEquals(request, fromDao);

        request.setItems(newItems);
        requestDao.updateRequest(request);
        assertNotEquals(request, fromDao);

        fromDao = requestDao.getRequestById(request.getId());

        assertEquals(request, fromDao);
    }

    /**
     * Test of delteRequest method, of class RequestDB.
     */
    @Test
    public void testDelteRequest() {
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
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
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

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        items = new ArrayList<>();
        items.add(item);

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setLocationId(location.getId());
        request.setItems(items);
        request = requestDao.addRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);

        location.setRequests(requests);
        Request fromDao = requestDao.getRequestById(request.getId());
        assertEquals(request, fromDao);

        requestDao.deleteRequest(request.getId());
        fromDao = requestDao.getRequestById(request.getId());

        assertNull(fromDao);
    }

    /**
     * Test of getAllRequestsByUser method, of class RequestDB.
     */
    @Test
    public void testGetAllRequestsByUser() {
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
        user2.setUsername("testUsername2");
        user2.setPassword("testPassword2");
        user2.setEnabled(true);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);

        Category category = new Category();
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setInInventory(3);
        item.setMax(10);
        item.setMin(5);
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

        Location location2 = new Location();
        location2.setName("test location2 name");
        location2.setDescription("test location2 description");
        location2.setUser(user);
        location2.setItems(items);
        location2 = locationDao.addLocation(location2);

        Location location3 = new Location();
        location3.setName("test location3 name");
        location3.setDescription("test locatin3 description");
        location3.setUser(user2);
        location3.setItems(items);
        location3 = locationDao.addLocation(location3);
        

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setQuantity(7);
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        items = new ArrayList<>();
        items.add(item);

        Request request = new Request();
        request.setRequestDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setLocationId(location.getId());
        request.setItems(items);
        request = requestDao.addRequest(request);

        Request request2 = new Request();
        request2.setRequestDate(LocalDateTime.now().withNano(0));
        request2.setStatus(1);
        request2.setItems(items);
        request2.setLocationId(location.getId());
        request2 = requestDao.addRequest(request2);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);

        Request request3 = new Request();
        request3.setRequestDate(LocalDateTime.now().withNano(0));
        request3.setStatus(1);
        request3.setLocationId(location2.getId());
        request3.setItems(items);
        request3 = requestDao.addRequest(request3);
        List<Request> requests2 = new ArrayList<>();
        requests.add(request3);

        Request request4 = new Request();
        request4.setRequestDate(LocalDateTime.now().withNano(0));
        request4.setStatus(1);
        request4.setLocationId(location3.getId());
        request4.setItems(items);
        request4 = requestDao.addRequest(request4);
        List<Request> requests3 = new ArrayList<>();
        requests.add(request4);

        location.setRequests(requests);
        location2.setRequests(requests2);
        location3.setRequests(requests3);
        List<Request> fromDao = requestDao.getAllRequestsByUser(user);

        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(request));
        assertTrue(fromDao.contains(request2));
        assertTrue(fromDao.contains(request3));
        assertFalse(fromDao.contains(request4));
    }

}
