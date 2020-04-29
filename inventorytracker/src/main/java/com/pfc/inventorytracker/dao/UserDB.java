/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.RoleDB.RoleMapper;
import com.pfc.inventorytracker.entities.Role;
import com.pfc.inventorytracker.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class UserDB implements UserDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbc.query("SELECT * FROM user", new UserMapper());
        users = addRolesToUsers(users);
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            User user = jdbc.queryForObject("SELECT * FROM user WHERE username = ?", new UserMapper(), username);
            user.setRoles(getRolesForUser(user));
            return user;
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        jdbc.update("INSERT INTO user (username, password, enabled) VALUES (?,?,?)",
                user.getUsername(),
                user.getPassword(),
                user.isEnabled());
        insertRolesForUser(user);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        jdbc.update("UPDATE location SET username = ? WHERE username = ?", null, user.getUsername());
        jdbc.update("UPDATE user SET password = ?, enabled = ? WHERE username = ?",
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getUsername());        
        insertRolesForUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        jdbc.update("DELETE FROM user_role WHERE username = ?", username);
        jdbc.update("UPDATE location SET username = ? WHERE username = ?", null, username);
        jdbc.update("DELETE FROM user WHERE username = ?", username);
    }

    @Override
    public List<User> getAllUsersByRole(Role role) {
        List<User> users = jdbc.query("SELECT u.* FROM user u "
                + "JOIN user_role ur ON u.username = ur.username WHERE roleId=?",
                new UserMapper(),
                role.getId());
        users = addRolesToUsers(users);
        return users;
    }

    private List<User> addRolesToUsers(List<User> users) {
        for(User user : users){
            user.setRoles(getRolesForUser(user));
        }
        return users;
    }

    private Set<Role> getRolesForUser(User user) {
        List<Role> roleList = jdbc.query("SELECT r.* FROM role r "
                + "JOIN user_role ur ON r.id = ur.roleId WHERE ur.username = ?", new RoleMapper(), user.getUsername());
        Set<Role> roles = new HashSet<>();
        for(Role role : roleList){
            roles.add(role);
        }
        return roles;
    }

    private void insertRolesForUser(User user) {
        for(Role role : user.getRoles()){
            jdbc.update("INSERT INTO user_role(username, roleId) VALUES (?,?)",
                    user.getUsername(),
                    role.getId());
        }
    }
    
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int arg1) throws SQLException {
            User u = new User();
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            return u;
        }
        
    }
    
}
