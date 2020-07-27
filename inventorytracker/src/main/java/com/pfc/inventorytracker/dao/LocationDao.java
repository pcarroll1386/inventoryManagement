/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.User;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface LocationDao {
    List<Location> getAllLocations();
    Location getLocationById(int id);
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocation(int id);
}
