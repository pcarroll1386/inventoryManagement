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
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.Request;
import com.pfc.inventorytracker.entities.User;
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
            location.setUser(getUserForLocation(location));
            location.setRequests(getRequestsForLocation(location));
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
                location.getDescription(),
                location.getUser().getUsername());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        insertLocationItems(location);
        return location;
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        jdbc.update("UPDATE location SET name = ?, description = ?, username = ? WHERE id= ?",
                location.getName(),
                location.getDescription(),
                location.getUser().getUsername(),
                location.getId());
        jdbc.update("DELETE FROM location_item WHERE locationId = ?", location.getId());
        insertLocationItems(location);
    }

    @Override
    @Transactional
    public void deleteLocation(int id) {
        jdbc.update("DELETE FROM location_item WHERE locationId = ?", id);
        jdbc.update("DELETE FROM request WHERE locationId = ?", id);
        jdbc.update("DELETE FROM location WHERE id = ?", id);
    }

    @Override
    public List<Location> getAllLocationsByUser(User user) {
        List<Location> locations = jdbc.query("SELECT * FROM location where username = ?", new LocationMapper(), user.getUsername());
        locations = addItemsReqeustsAndUserToLocations(locations);
        return locations;
    }

    private List<Location> addItemsReqeustsAndUserToLocations(List<Location> locations) {
        for (Location location : locations) {
            location.setItems(getItemsForLocation(location));
            location.setUser(getUserForLocation(location));
            location.setRequests(getRequestsForLocation(location));
        }
        return locations;
    }

    private List<Item> getItemsForLocation(Location location) {
        List<Item> items = jdbc.query("SELECT i.*, li.inInventory, li.max, li.min FROM item i "
                + "JOIN location_item li ON i.id = li.itemId WHERE li.locationId = ?",
                new LocationItemMapper(),
                location.getId());
        return items;
    }

    private User getUserForLocation(Location location) {
        User user = jdbc.queryForObject("SELECT u.* FROM user u "
                + "JOIN location l ON u.username = l.username WHERE l.id = ?",
                new UserMapper(),
                location.getId());
        return user;
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
        }
        return requests;
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
