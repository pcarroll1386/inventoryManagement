/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllUsers method, of class UserDB.
     */
    @Test
    public void testGetAllUsers() {
    }

    /**
     * Test of getUserByUsername method, of class UserDB.
     */
    @Test
    public void testGetUserByUsername() {
    }

    /**
     * Test of addUser method, of class UserDB.
     */
    @Test
    public void testAddUser() {
    }

    /**
     * Test of updateUser method, of class UserDB.
     */
    @Test
    public void testUpdateUser() {
    }

    /**
     * Test of deleteUser method, of class UserDB.
     */
    @Test
    public void testDeleteUser() {
    }

    /**
     * Test of getAllUsersByRole method, of class UserDB.
     */
    @Test
    public void testGetAllUsersByRole() {
    }
    
}
