package com.pfc.inventorytrackerjpa.controllers;


import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    LocationRepository locationRepo;

    @GetMapping("/getAll")
    public List<Location> getAllLocations(){
        return locationRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public Location getLocationById(@PathVariable("id") UUID id){
        Location location = locationRepo.findById(id).orElse(null);
        return location;
    }

    @PostMapping(value="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Location createLocation(@RequestBody Location location){
        Location savedLocation = locationRepo.save(location);
        return savedLocation;
    }

    @PutMapping(value="/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Location editLocation(@RequestBody Location location){
        Location currentLocation = locationRepo.findById(location.getId()).orElse(null);
        currentLocation.setName(location.getName());
        currentLocation.setDescription(location.getDescription());
        currentLocation.setTemplate(location.isTemplate());
        Location updatedLocation = locationRepo.save(currentLocation);
        return updatedLocation;
    }
}
