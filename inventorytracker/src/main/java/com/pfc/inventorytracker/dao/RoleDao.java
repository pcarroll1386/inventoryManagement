/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Role;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface RoleDao {
    List<Role> getAllRoles();
    Role getRoleById(int id);
    Role addRole(Role role);
    void updateRole(Role role);
    void deleteRole(int id);
}
