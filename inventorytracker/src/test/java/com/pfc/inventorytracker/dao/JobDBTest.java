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
public class JobDBTest {

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

    public JobDBTest() {
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
     * Test of getAllJobs method, of class JobDB.
     */
    @Test
    public void testGetAllJobs() {
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

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

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
        supervisor.setEmployeeNumber(310);
        supervisor.setLocations(locations);
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

        Item jobItem2 = new Item();
        jobItem2.setId("MI0535");
        jobItem2.setName("test itemName2");
        jobItem2.setDescription("Test Description2");
        jobItem2.setNickname("test nickName2");
        jobItem2.setPrice(new BigDecimal("25.95"));
        jobItem2.setCategories(categories);

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);
        jobItems.add(jobItem2);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("test Name2");
        job2.setLocation(location);
        job2.setItems(jobItems);
        job2 = jobDao.addJob(job2);

        Job job3 = new Job();
        job3.setId(3);
        job3.setName("test Name3");
        job3.setLocation(location);
        job3.setItems(jobItems);
        job3 = jobDao.addJob(job3);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);

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
        
        List<Job> fromDao = jobDao.getAllJobs();
        
        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(job));
        assertTrue(fromDao.contains(job2));
        assertTrue(fromDao.contains(job3));

    }

    /**
     * Test of addJob & getJobById method, of class JobDB.
     */
    @Test
    public void testAddGetJobById() {
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

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

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
        supervisor.setEmployeeNumber(310);
        supervisor.setLocations(locations);
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

        Item jobItem2 = new Item();
        jobItem2.setId("MI0535");
        jobItem2.setName("test itemName2");
        jobItem2.setDescription("Test Description2");
        jobItem2.setNickname("test nickName2");
        jobItem2.setPrice(new BigDecimal("25.95"));
        jobItem2.setCategories(categories);

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);
        jobItems.add(jobItem2);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("test Name2");
        job2.setLocation(location);
        job2.setItems(jobItems);
        job2 = jobDao.addJob(job2);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);

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
        
        Job fromDao = jobDao.getJobById(job.getId());
        
        assertEquals(fromDao, job);
        
    }

    /**
     * Test of updateJob method, of class JobDB.
     */
    @Test
    public void testUpdateJob() {
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

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("test Name2");
        job2.setLocation(location);
        job2.setItems(jobItems);
        job2 = jobDao.addJob(job2);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);

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
        
        Job fromDao = jobDao.getJobById(job.getId());
        
        assertEquals(fromDao, job);
        
        List<Item> newJobItems = new ArrayList<>();
        newJobItems.add(jobItem);
        newJobItems.add(jobItem2);
        newJobItems.add(jobItem3);
        
        job.setItems(newJobItems);
        job.setTemplate(false);
        job.setLocation(location2);
        assertNotEquals(job, fromDao);
        
        jobDao.updateJob(job);
        
        fromDao = jobDao.getJobById(job.getId());
        
        assertEquals(job, fromDao);
    }

    /**
     * Test of deleteJob method, of class JobDB.
     */
    @Test
    public void testDeleteJob() {
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

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

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
        supervisor.setEmployeeNumber(310);
        supervisor.setLocations(locations);
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

        Item jobItem2 = new Item();
        jobItem2.setId("MI0535");
        jobItem2.setName("test itemName2");
        jobItem2.setDescription("Test Description2");
        jobItem2.setNickname("test nickName2");
        jobItem2.setPrice(new BigDecimal("25.95"));
        jobItem2.setCategories(categories);

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);
        jobItems.add(jobItem2);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("test Name2");
        job2.setLocation(location);
        job2.setItems(jobItems);
        job2 = jobDao.addJob(job2);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);

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
        
        Job fromDao = jobDao.getJobById(job.getId());
        
        assertEquals(fromDao, job);
        
        jobDao.deleteJob(job.getId());
        
        fromDao = jobDao.getJobById(job.getId());
        
        assertNull(fromDao);
    }

    /**
     * Test of getAllJobsForLocation method, of class JobDB.
     */
    @Test
    public void testGetAllJobsForLocation() {
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

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

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

        List<Item> jobItems = new ArrayList<>();
        jobItems.add(jobItem);
        jobItems.add(jobItem2);

        Job job = new Job();
        job.setId(0);
        job.setName("test Name");
        job.setLocation(location);
        job.setItems(jobItems);
        job = jobDao.addJob(job);

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("test Name2");
        job2.setLocation(location);
        job2.setItems(jobItems);
        job2 = jobDao.addJob(job2);

        Job job3 = new Job();
        job3.setId(3);
        job3.setName("test Name3");
        job3.setLocation(location2);
        job3.setItems(jobItems);
        job3 = jobDao.addJob(job3);

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

        List<Item> requestItems = new ArrayList<>();
        requestItems.add(requestItem);
        requestItems.add(requestItem2);

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
        
        List<Job> fromDao = jobDao.getAllJobsForLocation(location);
        
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(job));
        assertTrue(fromDao.contains(job2));
        assertFalse(fromDao.contains(job3));
    }

}
