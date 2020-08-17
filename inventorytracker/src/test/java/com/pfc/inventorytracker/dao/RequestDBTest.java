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

    @Autowired
    JobDao jobDao;

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
        List<Job> jobs = jobDao.getAllJobs();
        for (Job job : jobs) {
            jobDao.deleteJob(job.getId());
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

        Item item3 = new Item();
        item3.setId("MI0536");
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
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
        supervisor.setName("test name s");
        supervisor.setEmployeeNumber(319);
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

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);

        item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test itemName2");
        item2.setDescription("Test Description2");
        item2.setNickname("test nickName2");
        item2.setPrice(new BigDecimal("25.95"));
        item2.setCategories(categories);

        item3 = new Item();
        item3.setId("MI0536");
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);

        items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(items);

        Job job2 = new Job();
        job.setId(1);
        job.setName("test Name2");
        job.setLocation(location2);
        job.setItems(items);

        Job job3 = new Job();
        job.setId(3);
        job.setName("test Name3");
        job.setLocation(location3);
        job.setItems(items);

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

        item3 = new Item();
        item3.setId("MI0536");
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setQuantity(7);
        item3.setNickname("test nickName3");
        item3.setPrice(new BigDecimal("25.95"));
        item3.setCategories(categories);

        items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);

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

        Request request2 = new Request();
        request2.setSubmitDate(LocalDateTime.now().withNano(0));
        request2.setFilledDate(LocalDateTime.now().withNano(0));
        request2.setStatus(3);
        request2.setPriority(3);
        request2.setType(1);
        request2.setItems(items);
        request2.setLocation(location2);
        request2.setUser(user);
        request2 = requestDao.addRequest(request2);

        Request request3 = new Request();
        request3.setSubmitDate(LocalDateTime.now().withNano(0));
        request3.setFilledDate(LocalDateTime.now().withNano(0));
        request3.setStatus(3);
        request3.setPriority(3);
        request3.setType(1);
        request3.setItems(items);
        request3.setLocation(location3);
        request3.setUser(user);
        request3 = requestDao.addRequest(request3);

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
        location.setDescription("test location description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        List<Location> locations = new ArrayList<>();
        locations.add(location);

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name s");
        supervisor.setEmployeeNumber(319);
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

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Item requestItem = new Item();
        requestItem.setId("MI0534");
        requestItem.setName("test itemName");
        requestItem.setDescription("Test Description");
        requestItem.setQuantity(7);
        requestItem.setNickname("test nickName");
        requestItem.setPrice(new BigDecimal("25.95"));
        requestItem.setCategories(categories);

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);

        Request request = new Request();
        request.setSubmitDate(LocalDateTime.now().withNano(0));
        request.setFilledDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setPriority(0);
        request.setType(2);
        request.setNotes("Test notes 1");
        request.setItems(requestItems);
        request.setLocation(location);
        request.setUser(user);
        request = requestDao.addRequest(request);

        Request request2 = new Request();
        request2.setSubmitDate(LocalDateTime.now().withNano(0));
        request2.setFilledDate(LocalDateTime.now().withNano(0));
        request2.setStatus(3);
        request2.setPriority(3);
        request2.setType(1);
        request2.setItems(requestItems);
        request2.setLocation(location);
        request2.setUser(user);
        request2 = requestDao.addRequest(request2);

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
        location.setDescription("test location description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        List<Location> locations = new ArrayList<>();
        locations.add(location);

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name s");
        supervisor.setEmployeeNumber(319);
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

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Item requestItem = new Item();
        requestItem.setId("MI0534");
        requestItem.setName("test itemName");
        requestItem.setDescription("Test Description");
        requestItem.setQuantity(7);
        requestItem.setNickname("test nickName");
        requestItem.setPrice(new BigDecimal("25.95"));
        requestItem.setCategories(categories);

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);

        Request request = new Request();
        request.setSubmitDate(LocalDateTime.now().withNano(0));
        request.setFilledDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setPriority(0);
        request.setType(2);
        request.setNotes("Test notes 1");
        request.setItems(requestItems);
        request.setLocation(location);
        request.setUser(user);
        request = requestDao.addRequest(request);

        Request fromDao = requestDao.getRequestById(request.getId());

        assertEquals(request, fromDao);;

        Item requestItem2 = new Item();
        requestItem2.setId("MI0535");
        requestItem2.setName("test itemName2");
        requestItem2.setDescription("Test Description2");
        requestItem2.setQuantity(7);
        requestItem2.setNickname("test nickName2");
        requestItem2.setPrice(new BigDecimal("25.95"));
        requestItem2.setCategories(categories);
        requestItem2 = itemDao.addItem(requestItem2);

        requestItems.add(requestItem2);

        request.setItems(requestItems);
        request.setType(4);
        assertNotEquals(request, fromDao);
        requestDao.updateRequest(request);

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
        location.setDescription("test location description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        List<Location> locations = new ArrayList<>();
        locations.add(location);

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name s");
        supervisor.setEmployeeNumber(319);
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

        item = new Item();
        item.setId("MI0534");
        item.setName("test itemName");
        item.setDescription("Test Description");
        item.setNickname("test nickName");
        item.setPrice(new BigDecimal("25.95"));
        item.setCategories(categories);

        items = new ArrayList<>();
        items.add(item);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(items);

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

        Item item3 = new Item();
        item3.setId("MI0536");
        item3.setName("test itemName3");
        item3.setDescription("Test Description3");
        item3.setInInventory(3);
        item3.setMax(10);
        item3.setMin(5);
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

        User supervisor = new User();
        supervisor.setUsername("Test supervisor");
        supervisor.setPassword("Test supervisor password");
        supervisor.setEnabled(true);
        supervisor.setRoles(roles);
        supervisor.setName("test name s");
        supervisor.setEmployeeNumber(310);
        supervisor.setLocations(locations);
        supervisor = userDao.addUser(supervisor);

        locations.add(location3);

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

        Item jobItem2 = new Item();
        jobItem2.setId("MI0535");
        jobItem2.setName("test itemName2");
        jobItem2.setDescription("Test Description2");
        jobItem2.setNickname("test nickName2");
        jobItem2.setPrice(new BigDecimal("25.95"));
        jobItem2.setCategories(categories);

        Item jobItem3 = new Item();
        jobItem3.setId("MI0536");
        jobItem3.setName("test itemName3");
        jobItem3.setDescription("Test Description3");
        jobItem3.setNickname("test nickName3");
        jobItem3.setPrice(new BigDecimal("25.95"));
        jobItem3.setCategories(categories);

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);
        jobItems.add(jobItem2);
        jobItems.add(jobItem3);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);

        Job job2 = new Job();
        job.setId(1);
        job.setName("test Name2");
        job.setLocation(location2);
        job.setItems(jobItems);

        Job job3 = new Job();
        job.setId(3);
        job.setName("test Name3");
        job.setLocation(location3);
        job.setItems(jobItems);

        Item requestItem = new Item();
        requestItem.setId("MI0534");
        requestItem.setName("test itemName");
        requestItem.setDescription("Test Description");
        requestItem.setQuantity(7);
        requestItem.setNickname("test nickName");
        requestItem.setPrice(new BigDecimal("25.95"));
        requestItem.setCategories(categories);

        Item requestItem2 = new Item();
        requestItem2.setId("MI0535");
        requestItem2.setName("test itemName2");
        requestItem2.setDescription("Test Description2");
        requestItem2.setQuantity(7);
        requestItem2.setNickname("test nickName2");
        requestItem2.setPrice(new BigDecimal("25.95"));
        requestItem2.setCategories(categories);

        Item requestItem3 = new Item();
        requestItem3.setId("MI0536");
        requestItem3.setName("test itemName3");
        requestItem3.setDescription("Test Description3");
        requestItem3.setQuantity(7);
        requestItem3.setNickname("test nickName3");
        requestItem3.setPrice(new BigDecimal("25.95"));
        requestItem3.setCategories(categories);

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);
        requestItems.add(requestItem3);

        Request request = new Request();
        request.setSubmitDate(LocalDateTime.now().withNano(0));
        request.setFilledDate(LocalDateTime.now().withNano(0));
        request.setStatus(1);
        request.setPriority(0);
        request.setType(2);
        request.setNotes("Test notes 1");
        request.setItems(requestItems);
        request.setLocation(location);
        request.setUser(user);
        request = requestDao.addRequest(request);

        Request request2 = new Request();
        request2.setSubmitDate(LocalDateTime.now().withNano(0));
        request2.setFilledDate(LocalDateTime.now().withNano(0));
        request2.setStatus(3);
        request2.setPriority(3);
        request2.setType(1);
        request2.setItems(requestItems);
        request2.setUser(user);
        request2.setLocation(location2);
        request2 = requestDao.addRequest(request2);

        Request request3 = new Request();
        request3.setSubmitDate(LocalDateTime.now().withNano(0));
        request3.setFilledDate(LocalDateTime.now().withNano(0));
        request3.setStatus(3);
        request3.setPriority(3);
        request3.setType(1);
        request3.setItems(requestItems);
        request3.setLocation(location3);
        request3.setUser(supervisor);
        request3 = requestDao.addRequest(request3);

        List<Request> fromDao = requestDao.getAllRequestsByUser(user);

        assertEquals(2, fromDao.size());
//        assertTrue(fromDao.contains(request));
//        assertTrue(fromDao.contains(request2));
        assertFalse(fromDao.contains(request3));
    }

}
