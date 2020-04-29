/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pfcar
 */
@Repository
public class RoleDB implements RoleDao{
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public List<Role> getAllRoles() {
        return jdbc.query("SELECT * FROM role", new RoleMapper());
    }

    @Override
    public Role getRoleById(int id) {
        try{
            return jdbc.queryForObject("SELECT * FROM role WHERE id = ?", new RoleMapper(), id);                    
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional
    public Role addRole(Role role) {
        jdbc.update("INSERT INTO role (role) VALUES(?)", role.getRole());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        role.setId(newId);
        return role;
    }

    @Override
    public void updateRole(Role role) {
        jdbc.update("UPDATE role SET role= ? WHERE id =?",
                role.getRole(),
                role.getId());
    }

    @Override
    @Transactional
    public void deleteRole(int id) {
        jdbc.update("DELETE FROM user_role WHERE roleId = ?", id);
        jdbc.update("DELETE FROM role WHERE id = ?", id);
    }
    
    public static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int arg1) throws SQLException {
            Role r = new Role();
            r.setId(rs.getInt("id"));
            r.setRole(rs.getString("role"));
            return r;
        }
        
    }
    
}
