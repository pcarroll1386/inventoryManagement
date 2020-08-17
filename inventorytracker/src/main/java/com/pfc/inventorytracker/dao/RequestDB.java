/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.ItemDB.RequestItemMapper;
import com.pfc.inventorytracker.dao.LocationDB.LocationMapper;
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
public class RequestDB implements RequestDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Request> getAllRequests() {
        List<Request> requests = jdbc.query("SELECT * FROM request", new RequestMapper());
        requests = addItemsForRequests(requests);
        requests = addLocationAndUserToRequests(requests);
        return requests;
    }

    @Override
    public Request getRequestById(int id) {
        try {
            Request request = jdbc.queryForObject("SELECT * FROM request WHERE id = ?", new RequestMapper(), id);
            request.setItems(getItemsForRequest(request));
            request.setLocation(getLocationForRequest(request));
            request.setUser(getUserForRequest(request));
            return request;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Request addRequest(Request request) {
        jdbc.update("INSERT INTO request(locationId, username, submitDate, fillDate, notes, status, type, priority, workOrder) VALUES(?,?,?,?,?,?,?,?,?)",
                request.getLocation().getId(),
                request.getUser().getUsername(),
                request.getSubmitDate(),
                request.getFilledDate(),
                request.getNotes(),
                request.getStatus(),
                request.getType(),
                request.getPriority(),
                request.getWorkOrder());
                
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        request.setId(newId);
        insertRequestItems(request);
        return request;
    }

    @Override
    @Transactional
    public void updateRequest(Request request) {
        jdbc.update("UPDATE request SET locationId = ?, username = ?, submitDate = ?, fillDate = ?, notes = ?, status = ?, type = ?, priority = ?, workOrder = ? WHERE  id = ?",
                request.getLocation().getId(),
                request.getUser().getUsername(),
                request.getSubmitDate(),
                request.getFilledDate(),
                request.getNotes(),
                request.getStatus(),
                request.getType(),
                request.getPriority(),
                request.getWorkOrder(),
                request.getId());
        jdbc.update("DELETE FROM request_item WHERE requestId = ?", request.getId());
        insertRequestItems(request);
    }

    @Override
    @Transactional
    public void deleteRequest(int id) {
        jdbc.update("DELETE FROM request_item WHERE requestId = ?", id);
        jdbc.update("DELETE FROM request WHERE id = ?", id);
    }

    @Override
    public List<Request> getAllRequestsByUser(User user) {
        List<Request> requests = jdbc.query("SELECT * FROM request WHERE username = ?", new RequestMapper(), user.getUsername());
        requests = addItemsForRequests(requests);
        requests = addLocationAndUserToRequests(requests);
        return requests;
    }

    @Override
    public List<Request> getAllRequestsByLocation(Location location) {
        List<Request> requests = jdbc.query("SELECT * FROM requests WHERE locationId = ?", new RequestMapper(), location.getId());
        requests = addItemsForRequests(requests);
        requests = addLocationAndUserToRequests(requests);
        return requests;
    }

    private List<Request> addItemsForRequests(List<Request> requests) {
        for (Request request : requests) {
            request.setItems(getItemsForRequest(request));
        }
        return requests;
    }

    private List<Item> getItemsForRequest(Request request) {
        List<Item> items = jdbc.query("SELECT i.*, ri.quantity FROM item i "
                + "JOIN request_item ri ON i.id = ri.itemId WHERE ri.requestId = ?",
                new RequestItemMapper(),
                request.getId());
        for(Item item : items){
            item.setCategories(getCategoriesforItem(item));
        }
        return items;
    }

    private Set<Category> getCategoriesforItem(Item item) {
        List<Category> categories = jdbc.query("SELECT c.* FROM category c "
                + "JOIN item_category ic ON c.id = ic.categoryId WHERE itemId = ?", new CategoryDB.CategoryMapper(), item.getId());
        Set<Category> categorySet = new HashSet<>();
        for (Category c : categories) {
            categorySet.add(c);
        }
        return categorySet;
    }


    private void insertRequestItems(Request request) {
        for(Item item : request.getItems()){
        jdbc.update("INSERT INTO request_item(requestId, itemId, quantity) VALUES (?,?,?)",
                request.getId(),
                item.getId(),
                item.getQuantity());
        }
    }

    private List<Request> addLocationAndUserToRequests(List<Request> requests) {
        for(Request request : requests){
            request.setLocation(getLocationForRequest(request));
            request.setUser(getUserForRequest(request));
        }
        return requests;
    }

    private Location getLocationForRequest(Request request) {
        Location location = jdbc.queryForObject("SELECT l.* FROM location l "
                + "JOIN request r ON l.id = r.locationId WHERE r.id = ?", new LocationMapper(), request.getId());
        location.setItems(getItemsForLocation(location));
        return location;
    }
    
    private List<Item> getItemsForLocation(Location location) {
        List<Item> items = jdbc.query("SELECT i.*, li.inInventory, li.max, li.min FROM item i "
                + "JOIN location_item li ON i.id = li.itemId WHERE li.locationId = ?",
                new ItemDB.LocationItemMapper(),
                location.getId());
        items = getCategorysForItems(items);
        return items;
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

    private User getUserForRequest(Request request) {
        User user = jdbc.queryForObject("SELECT u.* FROM user u "
                + "JOIN request r ON u.username = r.username WHERE r.id = ?", new UserMapper(), request.getId());
        user = addRolesLocationAndSupervisorToUsers(user);
        return user;
    }
    
    private User addRolesLocationAndSupervisorToUsers(User user) {
            user.setRoles(getRolesForUser(user));
            user.setLocations(getLocationsforUser(user));
            user.setSupervisor(getSupervisorForUser(user));
        return user;
    }

    private Set<Role> getRolesForUser(User user) {
        List<Role> roleList = jdbc.query("SELECT r.* FROM role r "
                + "JOIN user_role ur ON r.id = ur.roleId WHERE ur.username = ?", new RoleDB.RoleMapper(), user.getUsername());
        Set<Role> roles = new HashSet<>();
        for (Role role : roleList) {
            roles.add(role);
        }
        return roles;
    }

    private List<Location> getLocationsforUser(User user) {
        List<Location> locations = jdbc.query("SELECT l.* FROM location l "
                + "JOIN user_location ul ON l.id = ul.locationId WHERE ul.username = ?", new LocationMapper(), user.getUsername());   
        for(Location l : locations){
            l.setItems(getItemsForLocation(l));
        }
        return locations;
    }

    private User getSupervisorForUser(User user) {
        User supervisor = new User();
        try {
            supervisor = jdbc.queryForObject("SELECT u.* FROM user u "
                    + "JOIN user p ON u.username = p.supervisorId WHERE p.username= ?", new UserMapper(), user.getUsername());
            supervisor.setRoles(getRolesForUser(supervisor));
        } catch (DataAccessException ex) {
            return null;
        }
        return supervisor;
    }
    
    public static final class RequestMapper implements RowMapper<Request> {

        @Override
        public Request mapRow(ResultSet rs, int arg1) throws SQLException {
            Request r = new Request();
            r.setId(rs.getInt("id"));
            r.setSubmitDate(rs.getTimestamp("submitDate").toLocalDateTime());
            r.setFilledDate(rs.getTimestamp("fillDate").toLocalDateTime());
            r.setNotes(rs.getString("notes"));
            r.setStatus(rs.getInt("status"));
            r.setType(rs.getInt("type"));
            r.setPriority(rs.getInt("priority"));
            r.setWorkOrder(rs.getString("workOrder"));
            return r;
        }

    }

}
