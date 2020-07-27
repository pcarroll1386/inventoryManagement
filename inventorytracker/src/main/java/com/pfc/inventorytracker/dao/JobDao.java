/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfc.inventorytracker.dao;

import com.pfc.inventorytracker.entities.Job;
import com.pfc.inventorytracker.entities.Location;
import java.util.List;

/**
 *
 * @author pfcar
 */
public interface JobDao {
    List<Job> getAllJobs();
    Job getJobById(int id);
    Job addJob(Job job);
    void updateJob(Job job);
    void deleteJob(int id);
}
