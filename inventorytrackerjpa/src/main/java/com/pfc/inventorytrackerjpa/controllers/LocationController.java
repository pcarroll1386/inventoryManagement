package com.pfc.inventorytrackerjpa.controllers;


import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.InvalidMidiDataException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    LocationRepository locationRepo;

    @GetMapping("/getAllLocations")
    public List<Location> getAllLocations(){
        return locationRepo.findAll();
    }


    @PostMapping(value="/createLocation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Location createLocation(@RequestBody Location location){
        Location savedLocation = locationRepo.save(location);
        return savedLocation;
    }
}
