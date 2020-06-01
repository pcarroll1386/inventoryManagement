/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.controllers;

import com.pfc.inventorytracker.dao.CategoryDao;
import com.pfc.inventorytracker.dao.ItemDao;
import com.pfc.inventorytracker.dao.LocationDao;
import com.pfc.inventorytracker.dao.RequestDao;
import com.pfc.inventorytracker.dao.RoleDao;
import com.pfc.inventorytracker.dao.UserDao;
import com.pfc.inventorytracker.entities.Location;
import com.pfc.inventorytracker.entities.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author pfcar
 */
@Controller
public class LocationController {
    @Autowired
    CategoryDao categoryDao;
    
    @Autowired
    ItemDao itemDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    RequestDao requestDao;
    
    @Autowired
    RoleDao roleDao;
    
    @Autowired
    UserDao userDao;
    
    @GetMapping("inventory")
    public String inventoryPage(String username, Model model){
        User user = userDao.getUserByUsername(username);
        List<Location> locations = locationDao.getAllLocationsByUser(user);
        model.addAttribute("locations", locations);
        return "inventory";
    }
    
}
