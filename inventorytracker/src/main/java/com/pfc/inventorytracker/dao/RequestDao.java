/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.Request;
import com.pfc.inventorytracker.entities.User;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface RequestDao {
    List<Request> getAllRequests();
    Request getRequestById(int id);
    Request addRequest(Request request);
    void updateRequest(Request request);
    void delteRequest(int id);
    List<Request> getAllRequestsByLocation(Location location);
    List<Request> getAllRequestsByUser(User user);
}
