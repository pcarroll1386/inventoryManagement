/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Role;
import com.pfc.inventorytracker.entities.User;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface UserDao {
    List<User> getAllUsers();
    User getUserByUsername(String username);
    User addUser(User user);
    void updateUser(User user);
    void deleteUser(String username);
    List<User> getAllUsersByRole(Role role);
}
