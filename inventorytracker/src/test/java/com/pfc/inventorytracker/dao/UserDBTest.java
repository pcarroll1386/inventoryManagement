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
public class UserDBTest {
    
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
    
    public UserDBTest() {
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
            itemDao.deleteItemById(item.getId());
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
     * Test of getAllUsers method, of class UserDB.
     */
    @Test
    public void testGetAllUsers() {
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

        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(false);
        user2.setSupervisor(user);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);
        
        List<User> users = userDao.getAllUsers();        

        assertEquals(2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
    }

    /**
     * Test of getUserByUsername method & getUserByUsername method, of class
     * UserDB.
     */
    @Test
    public void testAddGetUser() {
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

        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(false);
        user2.setSupervisor(user);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);

        User fromDao = userDao.getUserByUsername(user2.getUsername());

        assertEquals(user2, fromDao);
    }

    /**
     * Test of updateUser method, of class UserDB.
     */
    @Test
    public void testUpdateUser() {
        Role role = new Role();
        role.setRole("ROLE_TEST");
        role = roleDao.addRole(role);

        Role role2 = new Role();
        role2.setRole("ROLE_TEST2");
        role2 = roleDao.addRole(role2);

        Role role3 = new Role();
        role3.setRole("ROLE_TEST3");
        role3 = roleDao.addRole(role3);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(role2);

        User user = new User();
        user.setUsername("test username");
        user.setPassword("test password");
        user.setEnabled(true);
        user.setRoles(roles);
        user = userDao.addUser(user);

        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(true);
        List<User> employees = new ArrayList<>();
        employees.add(user);
        user2.setSupervisor(user);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);
        
        User user3 = new User();
        user3.setUsername("test username3");
        user3.setPassword("test password3");
        user3.setEnabled(true);
        user3.setRoles(roles);
        user3 = userDao.addUser(user3);
        
        User fromDao = userDao.getUserByUsername(user.getUsername());
        assertEquals(user, fromDao);
        
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);
        newRoles.add(role3);
        
        Category category = new Category();
        category.setName("test name");
        category = categoryDao.addCategory(category);

        Category category2 = new Category();
        category2.setName("test2 name");
        category2 = categoryDao.addCategory(category2);

        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test name");
        item.setDescription("test description");
        item.setInInventory(5);
        item.setMax(10);
        item.setMin(4);
        item.setName("test nickname");
        item.setPrice(new BigDecimal("25.56"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test2 name");
        item2.setDescription("test2 description");
        item2.setInInventory(5);
        item2.setMax(10);
        item2.setMin(4);
        item2.setName("test2 nickname");
        item2.setPrice(new BigDecimal("25.97"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        List<Item> items = new ArrayList<>();

        Location location = new Location();
        location.setName("Test name");
        location.setUser(user);
        location.setDescription("test Description");
        location.setItems(items);
        location = locationDao.addLocation(location);
        
        user.setRoles(newRoles);
        user.setSupervisor(user3);
        userDao.updateUser(user);

        assertNotEquals(user, fromDao);

        fromDao = userDao.getUserByUsername(user.getUsername());

        assertEquals(user, fromDao);
    }

    /**
     * Test of deleteUser method, of class UserDB.
     */
    @Test
    public void testDeleteUser() {
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

        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(false);
        user2.setSupervisor(user);
        user2.setRoles(roles);
        user2 = userDao.addUser(user2);

        Category category = new Category();
        category.setName("test name");
        category = categoryDao.addCategory(category);

        Category category2 = new Category();
        category2.setName("test2 name");
        category2 = categoryDao.addCategory(category2);

        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);

        Item item = new Item();
        item.setId("MI0534");
        item.setName("test name");
        item.setDescription("test description");
        item.setInInventory(5);
        item.setMax(10);
        item.setMin(4);
        item.setName("test nickname");
        item.setPrice(new BigDecimal("25.56"));
        item.setCategories(categories);
        item = itemDao.addItem(item);

        Item item2 = new Item();
        item2.setId("MI0535");
        item2.setName("test2 name");
        item2.setDescription("test2 description");
        item2.setInInventory(5);
        item2.setMax(10);
        item2.setMin(4);
        item2.setName("test2 nickname");
        item2.setPrice(new BigDecimal("25.97"));
        item2.setCategories(categories);
        item2 = itemDao.addItem(item2);

        List<Item> items = new ArrayList<>();

        Location location = new Location();
        location.setName("Test name");
        location.setUser(user);
        location.setDescription("test Description");
        location.setItems(items);
        location = locationDao.addLocation(location);

        userDao.deleteUser(user.getUsername());

        User fromDao = userDao.getUserByUsername(user.getUsername());

        assertNull(fromDao);
    }

    /**
     * Test of getAllUsersByRole method, of class UserDB.
     */
    @Test
    public void testGetAllUsersByRole() {
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

        User user2 = new User();
        user2.setUsername("test username2");
        user2.setPassword("test password2");
        user2.setEnabled(false);
        user2.setRoles(roles);
        user2.setSupervisor(user);
        user2 = userDao.addUser(user2);

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);

        User user3 = new User();
        user3.setUsername("test username3");
        user3.setPassword("test password3");
        user3.setEnabled(false);
        user3.setRoles(newRoles);
        user3 = userDao.addUser(user3);

        List<User> users = userDao.getAllUsersByRole(role);

        assertEquals(3, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
        assertTrue(users.contains(user3));

        users = userDao.getAllUsersByRole(role2);

        assertEquals(2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
        assertFalse(users.contains(user3));
    }
    
}
