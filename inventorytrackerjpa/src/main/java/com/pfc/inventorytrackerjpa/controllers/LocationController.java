package com.pfc.inventorytrackerjpa.controllers;


import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.services.InvalidLocationException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationRepository locationRepo;

    public LocationController(LocationRepository locationRepo) {
        this.locationRepo = locationRepo;
    }

    @GetMapping("/getAll")
    public List<Location> getAllLocations(){
        return locationRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public Location getLocationById(@PathVariable("id") Long id){
        Location location = locationRepo.findById(id).orElse(null);
        return location;
    }

    @PostMapping(value="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Location createLocation(@RequestBody Location location){
        Location savedLocation = locationRepo.save(location);
        return savedLocation;
    }

    @PutMapping(value="/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Location editLocation(@RequestBody Location location) throws InvalidLocationException {
        Location currentLocation = locationRepo.findById(location.getId()).orElse(null);
        if(currentLocation == null){
            throw new InvalidLocationException("Please provide valid location");
        }
        currentLocation.setName(location.getName());
        currentLocation.setDescription(location.getDescription());
        currentLocation.setTemplate(location.isTemplate());
        Location updatedLocation = locationRepo.save(currentLocation);
        return updatedLocation;
    }

    @PostMapping("/delete/{id}")
    public boolean deleteLocationById(@PathVariable("id") Long id ){
        Location location = locationRepo.findById(id).orElse(null);
        if(location != null){
            locationRepo.delete(location);
            return true;
        }
        return false;
    }
}
