/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Job;
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

    @Autowired
    JobDao jobDao;

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
        List<Job> jobs = jobDao.getAllJobs();
        for (Job job : jobs) {
            jobDao.deleteJob(job.getId());
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
        user.setName("test name");
        user.setEmployeeNumber(318);
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
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        Item item3 = new Item();
        item3.setId("MI0536");
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
        user.setName("test name");
        user.setEmployeeNumber(318);
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

        Category category = new Category();
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item locationItem = new Item();
        locationItem.setId("MI0534");
        locationItem.setName("test itemName");
        locationItem.setDescription("Test Description");
        locationItem.setNickname("test nickName");
        locationItem.setPrice(new BigDecimal("25.95"));
        locationItem.setCategories(categories);  
        locationItem.setInInventory(3);
        locationItem.setMax(10);
        locationItem.setMin(5);

        List<Item> items = new ArrayList<>();
        items.add(locationItem);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test location description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("test location2 name");
        location2.setDescription("test locatin2 description");
        location2.setItems(items);
        location2 = locationDao.addLocation(location2);

        Location location3 = new Location();
        location3.setName("test location3 name");
        location3.setDescription("test locatin3 description");
        location3.setItems(items);
        location3 = locationDao.addLocation(location3);

        List<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location2);
        locations.add(location3);

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name");
        supervisor.setEmployeeNumber(318);
        supervisor = userDao.addUser(supervisor);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user.setName("test name");
        user.setEmployeeNumber(318);
        user.setLocations(locations);
        user.setSupervisor(supervisor);
        user = userDao.addUser(user);

        Item jobItem = new Item();
        jobItem.setId("MI0534");
        jobItem.setName("test itemName");
        jobItem.setDescription("Test Description");
        jobItem.setNickname("test nickName");
        jobItem.setPrice(new BigDecimal("25.95"));
        jobItem.setCategories(categories);

        items = new ArrayList<>();
        items.add(jobItem);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(items);
        jobDao.addJob(job);

        Item RequestIitem = new Item();
        RequestIitem.setId("MI0534");
        RequestIitem.setName("test itemName");
        RequestIitem.setDescription("Test Description");
        RequestIitem.setQuantity(7);
        RequestIitem.setNickname("test nickName");
        RequestIitem.setPrice(new BigDecimal("25.95"));
        RequestIitem.setCategories(categories);

        items = new ArrayList<>();
        items.add(RequestIitem);

        Request request = new Request();
        request.setSubmitDate(LocalDateTime.now().withNano(0));
        request.setFilledDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setPriority(0);
        request.setType(2);
        request.setNotes("Test notes 1");
        request.setItems(items);
        request.setLocation(location);
        request.setUser(user);
        request = requestDao.addRequest(request);

        Item fromDao = itemDao.getItemById(item.getId());
        assertEquals(item, fromDao);

        Category category2 = new Category();
        category2.setName("Test name 2");
        category2 = categoryDao.addCategory(category2);

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

        Category category = new Category();
        category.setName("Test Category");
        category = categoryDao.addCategory(category);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item locationItem = new Item();
        locationItem.setId("MI0534");
        locationItem.setName("test itemName");
        locationItem.setDescription("Test Description");
        locationItem.setNickname("test nickName");
        locationItem.setPrice(new BigDecimal("25.95"));
        locationItem.setCategories(categories);  
        locationItem.setInInventory(3);
        locationItem.setMax(10);
        locationItem.setMin(5);

        List<Item> items = new ArrayList<>();
        items.add(locationItem);

        Location location = new Location();
        location.setName("test location name");
        location.setDescription("test location description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("test location2 name");
        location2.setDescription("test locatin2 description");
        location2.setItems(items);
        location2 = locationDao.addLocation(location2);

        Location location3 = new Location();
        location3.setName("test location3 name");
        location3.setDescription("test locatin3 description");
        location3.setItems(items);
        location3 = locationDao.addLocation(location3);

        List<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location2);
        locations.add(location3);

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name");
        supervisor.setEmployeeNumber(318);
        supervisor = userDao.addUser(supervisor);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setRoles(roles);
        user.setName("test name");
        user.setEmployeeNumber(318);
        user.setLocations(locations);
        user.setSupervisor(supervisor);
        user = userDao.addUser(user);

        Item jobItem = new Item();
        jobItem.setId("MI0534");
        jobItem.setName("test itemName");
        jobItem.setDescription("Test Description");
        jobItem.setNickname("test nickName");
        jobItem.setPrice(new BigDecimal("25.95"));
        jobItem.setCategories(categories);

        items = new ArrayList<>();
        items.add(jobItem);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(items);
        jobDao.addJob(job);

        Item RequestIitem = new Item();
        RequestIitem.setId("MI0534");
        RequestIitem.setName("test itemName");
        RequestIitem.setDescription("Test Description");
        RequestIitem.setQuantity(7);
        RequestIitem.setNickname("test nickName");
        RequestIitem.setPrice(new BigDecimal("25.95"));
        RequestIitem.setCategories(categories);

        items = new ArrayList<>();
        items.add(RequestIitem);

        Request request = new Request();
        request.setSubmitDate(LocalDateTime.now().withNano(0));
        request.setFilledDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setPriority(0);
        request.setType(2);
        request.setNotes("Test notes 1");
        request.setItems(items);
        request.setLocation(location);
        request.setUser(user);
        request = requestDao.addRequest(request);

        Item fromDao = itemDao.getItemById(item.getId());
        assertEquals(item, fromDao);

        itemDao.deleteItemById(item.getId());

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
        user.setName("test name");
        user.setEmployeeNumber(318);
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
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        Item item3 = new Item();
        item3.setId("MI0536");
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
