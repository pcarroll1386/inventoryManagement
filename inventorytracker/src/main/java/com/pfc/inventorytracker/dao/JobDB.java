/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.dao.ItemDB.ItemMapper;
import com.pfc.inventorytracker.dao.LocationDB.LocationMapper;
import com.pfc.inventorytracker.entities.Category;
import com.pfc.inventorytracker.entities.Item;
import com.pfc.inventorytracker.entities.Job;
import com.pfc.inventorytracker.entities.Location;
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
public class JobDB implements JobDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = jdbc.query("SELECT * FROM job", new JobMapper());
        jobs = addLocationsAndItemsToJobs(jobs);
        return jobs;
    }

    @Override
    public Job getJobById(int id) {
        try{
            Job job = new Job();
            job = jdbc.queryForObject("SELECT * FROM job WHERE id = ?", new JobMapper(), id);
            job.setLocation(getLocationForJob(job));
            job.setItems(getItemsForJob(job));
            return job;
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional
    public Job addJob(Job job) {
        jdbc.update("INSERT INTO job (locationId, name, template) VALUES (?,?,?)",
                job.getLocation().getId(),
                job.getName(),
                job.isTemplate());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        job.setId(newId);
        insertJobItems(job);
        return job;        
    }

    @Override
    @Transactional
    public void updateJob(Job job) {
        jdbc.update("UPDATE job SET locationId = ?, name = ?, template = ? WHERE id = ?",
                job.getLocation().getId(),
                job.getName(),
                job.isTemplate(),
                job.getId());
        jdbc.update("DELETE FROM job_item WHERE jobId = ?", job.getId());
        insertJobItems(job);
    }

    @Override
    @Transactional
    public void deleteJob(int id) {
        jdbc.update("DELETE FROM job_item WHERE jobId = ?", id);
        jdbc.update("DELETE FROM job WHERE id = ?", id);
    }

    @Override
    public List<Job> getAllJobsForLocation(Location location) {
        List<Job> jobs = jdbc.query("SELECT * FROM job WHERE locationId = ?", new JobMapper(), location.getId());
        jobs = addLocationsAndItemsToJobs(jobs);
        return jobs;
    }

    private List<Job> addLocationsAndItemsToJobs(List<Job> jobs) {
        for(Job job : jobs){
            job.setLocation(getLocationForJob(job));
            job.setItems(getItemsForJob(job));
        }
        return jobs;
    }

    private Location getLocationForJob(Job job) {
        Location location = jdbc.queryForObject("SELECT l.* FROM location l "
                + "JOIN job j ON l.id = j.locationID WHERE j.id = ?", new LocationMapper(), job.getId());
        location.setItems(getItemsForLocation(location));
        return location;
    }
    private List<Item> getItemsForLocation(Location location) {
        List<Item> items = jdbc.query("SELECT i.*, li.inInventory, li.max, li.min FROM item i "
                + "JOIN location_item li ON i.id = li.itemId WHERE li.locationId = ?",
                new ItemDB.LocationItemMapper(),
                location.getId());
        items = getCategoriesForItems(items);
        return items;
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

    private List<Item> getItemsForJob(Job job) {
        List<Item> items = jdbc.query("SELECT i.* FROM item i "
                + "JOIN job_item ji ON i.id = ji.itemId WHERE  ji.jobId = ?", new ItemMapper(), job.getId());
        items = getCategoriesForItems(items);
        return items;
    }

    private void insertJobItems(Job job) {
        for(Item item : job.getItems()){
            jdbc.update("INSERT INTO job_item (jobId, itemId) VALUES (?,?)",
                    job.getId(),
                    item.getId());
        }
    }
    
    public static final class JobMapper implements RowMapper<Job> {

        @Override
        public Job mapRow(ResultSet rs, int arg1) throws SQLException {
            Job j = new Job();
            j.setId(rs.getInt("id"));
            j.setName(rs.getString("name"));
            j.setTemplate(rs.getBoolean("template"));
            return j;
        }

    }
    
}
