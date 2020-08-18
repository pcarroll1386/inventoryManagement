/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.LocationDB.LocationMapper;
import com.pfc.inventorytracker.dao.RoleDB.RoleMapper;
import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Location;
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
public class UserDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbc.query("SELECT * FROM user", new UserMapper());
        users = addRolesLocationAndSupervisorToUsers(users);
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            User user = jdbc.queryForObject("SELECT * FROM user WHERE username = ?", new UserMapper(), username);
            user.setRoles(getRolesForUser(user));
            user.setLocations(getLocationsforUser(user));
            user.setSupervisor(getSupervisorForUser(user));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        jdbc.update("INSERT INTO user (username, password, enabled, name, employeeNumber) VALUES (?,?,?,?,?)",
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getName(),
                user.getEmployeeNumber());

        if (user.getSupervisor() != null) {
            jdbc.update("UPDATE user SET supervisorId = ? WHERE username =?",
                    user.getSupervisor().getUsername(),
                    user.getUsername());
        }
        insertLocationsForUser(user);
        insertRolesForUser(user);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        jdbc.update("DELETE FROM user_location WHERE username = ?", user.getUsername());
        jdbc.update("DELETE FROM user_role WHERE username = ?", user.getUsername());
        String username = null;
        if (user.getSupervisor() != null) {
            username = user.getSupervisor().getUsername();
        }
        jdbc.update("UPDATE user SET password = ?, enabled = ?, supervisorId = ? WHERE username = ?",
                user.getPassword(),
                user.isEnabled(),
                user.getSupervisor().getUsername(),
                user.getUsername());
        insertLocationsForUser(user);
        insertRolesForUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        jdbc.update("DELETE FROM user_role WHERE username = ?", username);
        jdbc.update("DELETE FROM user_location WHERE username = ?", username);
        jdbc.update("DELETE ri.* FROM request_item ri "
                + "JOIN request r ON ri.requestId = r.id WHERE r.username = ?", username);
        jdbc.update("DELETE FROM request WHERE username = ?", username);
        deleteSupervisorFromUsers(username);
        jdbc.update("DELETE FROM user WHERE username = ?", username);
    }

    @Override
    public List<User> getAllUsersByRole(Role role
    ) {
        List<User> users = jdbc.query("SELECT u.* FROM user u "
                + "JOIN user_role ur ON u.username = ur.username WHERE roleId=?",
                new UserMapper(),
                role.getId());
        users = addRolesLocationAndSupervisorToUsers(users);
        return users;
    }

    @Override
    public List<User> getAllBySupervisor(User supervisor) {
        List<User> users = jdbc.query("SELECT * FROM user WHERE supervisorId = ?", new UserMapper(), supervisor.getUsername());
        users = addRolesLocationAndSupervisorToUsers(users);
        return users;
    }

    @Override
    public List<User> getAllUsersByLocation(Location location) {
        List<User> users = jdbc.query("SELECT u.* FROM user u "
                + "JOIN user_location ul ON u.username = ul.username WHERE ul.locationId = ?", new UserMapper(), location.getId());
        users = addRolesLocationAndSupervisorToUsers(users);
        return users;
    }

    private List<User> addRolesLocationAndSupervisorToUsers(List<User> users) {
        for (User user : users) {
            user.setRoles(getRolesForUser(user));
            user.setLocations(getLocationsforUser(user));
            user.setSupervisor(getSupervisorForUser(user));
        }
        return users;
    }

    private Set<Role> getRolesForUser(User user) {
        List<Role> roleList = jdbc.query("SELECT r.* FROM role r "
                + "JOIN user_role ur ON r.id = ur.roleId WHERE ur.username = ?", new RoleMapper(), user.getUsername());
        Set<Role> roles = new HashSet<>();
        for (Role role : roleList) {
            roles.add(role);
        }
        return roles;
    }

    private List<Location> getLocationsforUser(User user) {
        List<Location> locations = jdbc.query("SELECT l.* FROM location l "
                + "JOIN user_location ul ON l.id = ul.locationId WHERE ul.username = ?", new LocationMapper(), user.getUsername());
        locations = addItemsToLocations(locations);
        if(locations.size() == 0){
            return null;
        }
        return locations;
    }
    
    private List<Location> addItemsToLocations(List<Location> locations) {
        for (Location location : locations) {
            location.setItems(getItemsForLocation(location));
        }
        return locations;
    }

    private List<Item> getItemsForLocation(Location location) {
        List<Item> items = jdbc.query("SELECT i.*, li.inInventory, li.max, li.min FROM item i "
                + "JOIN location_item li ON i.id = li.itemId WHERE li.locationId = ?",
                new ItemDB.LocationItemMapper(),
                location.getId());
        items = getCategoriesForItems(items);
        return items;
    }

    private User getSupervisorForUser(User user) {
        User supervisor = new User();
        try {
            supervisor = jdbc.queryForObject("SELECT u.* FROM user u "
                    + "JOIN user p ON u.username = p.supervisorId WHERE p.username= ?", new UserMapper(), user.getUsername());
            supervisor.setRoles(getRolesForUser(supervisor));
            supervisor.setSupervisor(getSupervisorForUser(supervisor));
            supervisor.setLocations(getLocationsforUser(supervisor));
        } catch (DataAccessException ex) {
            return null;
        }
        return supervisor;
    }

    private void insertRolesForUser(User user) {
        for (Role role : user.getRoles()) {
            jdbc.update("INSERT INTO user_role(username, roleId) VALUES (?,?)",
                    user.getUsername(),
                    role.getId());
        }
    }

    private void insertLocationsForUser(User user) {
        if (user.getLocations() != null) {
            for (Location location : user.getLocations()) {
                jdbc.update("INSERT INTO user_location(username, locationId) VALUES (?,?)",
                        user.getUsername(),
                        location.getId());
            }
        }
    }

    private void deleteSupervisorFromUsers(String username) {
        User user = getUserByUsername(username);
        List<User> users = getAllBySupervisor(user);
        if (users == null) {
            return;
        }
        for (User u : users) {
            jdbc.update("UPDATE user SET supervisorId = ? WHERE username = ?", null, u.getUsername());
        }
    }

    private List<Item> getCategoriesForItems(List<Item> items) {
        for (Item i : items) {
            i.setCategories(addCategoriesToItem(i));
        }
        return items;
    }
    
    private Set<Category> addCategoriesToItem(Item i) {
        List<Category> categories = jdbc.query("SELECT c.* FROM category c "
                + "JOIN item_category ic ON c.id = ic.categoryId WHERE itemId = ?", new CategoryDB.CategoryMapper(), i.getId());
        Set<Category> categorySet = new HashSet<>();
        for (Category c : categories) {
            categorySet.add(c);
        }
        return categorySet;
    }

    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int arg1) throws SQLException {
            User u = new User();
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setName(rs.getString("name"));
            u.setEmployeeNumber(rs.getInt("employeeNumber"));
            return u;
        }

    }

}
