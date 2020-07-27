/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.ItemDB.ItemMapper;
import com.pfc.inventorytracker.dao.ItemDB.LocationItemMapper;
import com.pfc.inventorytracker.dao.ItemDB.RequestItemMapper;
import com.pfc.inventorytracker.dao.RequestDB.RequestMapper;
import com.pfc.inventorytracker.dao.UserDB.UserMapper;
import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.Request;
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
public class LocationDB implements LocationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Location> getAllLocations() {
        List<Location> locations = jdbc.query("SELECT * FROM location", new LocationMapper());
        locations = addItemsReqeustsAndUserToLocations(locations);
        return locations;
    }

    @Override
    public Location getLocationById(int id) {
        try {
            Location location = jdbc.queryForObject("SELECT * FROM location WHERE id = ?", new LocationMapper(), id);
            location.setItems(getItemsForLocation(location));
            return location;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        jdbc.update("INSERT INTO location(name, description, username) VALUES (?,?,?)",
                location.getName(),
                location.getDescription());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        insertLocationItems(location);
        return location;
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        jdbc.update("UPDATE location SET name = ?, description = ?, template = ? WHERE id= ?",
                location.getName(),
                location.getDescription(),
                location.isTemplate(),
                location.getId());
        jdbc.update("DELETE FROM location_item WHERE locationId = ?", location.getId());
        insertLocationItems(location);
    }

    @Override
    @Transactional
    public void deleteLocation(int id) {
        jdbc.update("DELETE FROM user_location WHERE locationId = ?", id);
        jdbc.update("DELETE FROM location_item WHERE locationId = ?", id);
        jdbc.update("DELETE ri.* FROM request_item ri "
                + "JOIN request r ON ri.requestId = r.id WHERE r.locationId = ?", id);
        jdbc.update("DELETE FROM request WHERE locationId = ?", id);
        jdbc.update("DELETE FROM location WHERE id = ?", id);
    }

    @Override
    public List<Location> getAllLocationsByUser(User user) {
        List<Location> locations = jdbc.query("SELECT * FROM location where username = ?", new LocationMapper(), user.getUsername());
        locations = addItemsReqeustsAndUsersToLocations(locations);
        return locations;
    }

    private List<Location> addItemsReqeustsAndUsersToLocations(List<Location> locations) {
        for (Location location : locations) {
            location.setItems(getItemsForLocation(location));
        }
        return locations;
    }

    private List<Item> getItemsForLocation(Location location) {
        List<Item> items = jdbc.query("SELECT i.*, li.inInventory, li.max, li.min FROM item i "
                + "JOIN location_item li ON i.id = li.itemId WHERE li.locationId = ?",
                new LocationItemMapper(),
                location.getId());
        items = getCategorysForItems(items);
        return items;
    }

    private User getUserForLocation(Location location) {
        try{
        User user = jdbc.queryForObject("SELECT u.* FROM user u "
                + "JOIN location l ON u.username = l.username WHERE l.id = ?",
                new UserMapper(),
                location.getId());
        user.setRoles(getRolesForUser(user));
        return user;
        }catch(DataAccessException ex){
            return null;
        }
    }

    private List<Request> getRequestsForLocation(Location location) {
        List<Request> requests = jdbc.query("SELECT * FROM request WHERE locationId = ?",
                new RequestMapper(),
                location.getId());
        requests = getItemsForRequest(requests);
        return requests;
    }

    private List<Request> getItemsForRequest(List<Request> requests) {
        for (Request r : requests) {
            List<Item> items = jdbc.query("SELECT i.*, ri.quantity FROM item i "
                    + "JOIN request_item ri ON i.id = ri.itemId WHERE requestId =?", new RequestItemMapper(), r.getId());
            items = getCategorysForItems(items); 
            r.setItems(items);
        }
        return requests;
    }

    private List<Item> getCategorysForItems(List<Item> items) {
        for (Item i : items) {
            i.setCategories(getCategoriesForItem(i));
        }
        return items;
    }

    private Set<Category> getCategoriesForItem(Item i) {
        List<Category> categories = jdbc.query("SELECT c.* FROM category c "
                + "JOIN item_category ic ON c.id = ic.categoryId WHERE itemId = ?", new CategoryDB.CategoryMapper(), i.getId());
        Set<Category> categorySet = new HashSet<>();
        for (Category c : categories) {
            categorySet.add(c);
        }
        return categorySet;
    }

    private Set<Role> getRolesForUser(User user) {
        List<Role> roleList = jdbc.query("SELECT r.* FROM role r "
                + "JOIN user_role ur ON r.id = ur.roleId WHERE ur.username = ?", new RoleDB.RoleMapper(), user.getUsername());
        Set<Role> roles = new HashSet<>();
        for(Role role : roleList){
            roles.add(role);
        }
        return roles;
    }

    private void insertLocationItems(Location location) {
        for (Item item : location.getItems()) {
            jdbc.update("INSERT INTO location_item(locationId, itemId, inInventory, max, min) VALUES(?,?,?,?,?)",
                    location.getId(),
                    item.getId(),
                    item.getInInventory(),
                    item.getMax(),
                    item.getMin());
        }
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int arg1) throws SQLException {
            Location l = new Location();
            l.setId(rs.getInt("id"));
            l.setName(rs.getString("name"));
            l.setDescription(rs.getString("description"));
            return l;
        }

    }

}
