/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.ItemDB.RequestItemMapper;
import com.pfc.inventorytracker.dao.LocationDB.LocationMapper;
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
public class RequestDB implements RequestDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Request> getAllRequests() {
        List<Request> requests = jdbc.query("SELECT * FROM request", new RequestMapper());
        requests = addItemsForRequests(requests);
        return requests;
    }

    @Override
    public Request getRequestById(int id) {
        try {
            Request request = jdbc.queryForObject("SELECT * FROM request WHERE id = ?", new RequestMapper(), id);
            request.setItems(getItemsForRequest(request));
            return request;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Request addRequest(Request request) {
        jdbc.update("INSERT INTO request(requestDate, status, locationId) VALUES(?,?,?)",
                request.getRequestDate(),
                request.getStatus(),
                request.getLocationId());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        request.setId(newId);
        insertRequestItems(request);
        return request;
    }

    @Override
    @Transactional
    public void updateRequest(Request request) {
        jdbc.update("UPDATE request SET requestDate = ?, status = ?, locationId = ? WHERE  id = ?",
                request.getRequestDate(),
                request.getStatus(),
                request.getLocationId(),
                request.getId());
        jdbc.update("DELETE FROM request_item WHERE requestId = ?",
                request.getId());
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
        List<Request> requests = jdbc.query("SELECT r.* FROM request r "
                + "JOIN location l ON r.locationId = l.id WHERE username = ?", new RequestMapper(), user.getUsername());
        requests = addItemsForRequests(requests);
        return requests;
    }

    private List<Request> addItemsForRequests(List<Request> requests) {
        for (Request request : requests) {
            request.setItems(getItemsForRequest(request));
        }
        return requests;
    }

    private List<Item> getItemsForRequest(Request request) {
        return jdbc.query("SELECT i.*, ri.quantity FROM item i "
                + "JOIN request_item ri ON i.id = ri.itemId WHERE ri.requestId = ?",
                new RequestItemMapper(),
                request.getId());
    }

    private void insertRequestItems(Request request) {
        for(Item item : request.getItems()){
        jdbc.update("INSERT INTO request_item(requestId, itemId, quantity) VALUES (?,?,?)",
                request.getId(),
                item.getId(),
                item.getQuantity());
        }
    }

    public static final class RequestMapper implements RowMapper<Request> {

        @Override
        public Request mapRow(ResultSet rs, int arg1) throws SQLException {
            Request r = new Request();
            r.setId(rs.getInt("id"));
            r.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
            r.setStatus(rs.getInt("status"));
            r.setLocationId(rs.getInt("locationId"));
            return r;
        }

    }

}
