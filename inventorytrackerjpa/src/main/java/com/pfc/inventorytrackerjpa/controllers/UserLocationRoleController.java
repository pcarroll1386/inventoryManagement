package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.dto.UserLocationRoleRequest;
import com.pfc.inventorytrackerjpa.entities.UserLocationRole;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidLocationException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;
import com.pfc.inventorytrackerjpa.services.UserLocationRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/locationRoles")
public class UserLocationRoleController {

    private final UserLocationRoleService userLocationRoleService;

    public UserLocationRoleController(UserLocationRoleService userLocationRoleService) {
        this.userLocationRoleService = userLocationRoleService;
    }

    @GetMapping("/getAll")
    public List<UserLocationRole> getAll() {
        return userLocationRoleService.getAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<UserLocationRole> getByUser(@PathVariable("userId") long userId) {
        return userLocationRoleService.getByUser(userId);
    }

    @GetMapping("/getByLocation/{locationId}")
    public List<UserLocationRole> getByLocation(@PathVariable("locationId") long locationId) {
        return userLocationRoleService.getByLocation(locationId);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserLocationRole create(@Valid @RequestBody UserLocationRoleRequest request)
            throws InvalidDataException, InvalidUserException, InvalidLocationException, InvalidRoleException {
        return userLocationRoleService.create(request);
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserLocationRole edit(@PathVariable("id") long id, @Valid @RequestBody UserLocationRoleRequest request)
            throws InvalidDataException, InvalidUserException, InvalidLocationException, InvalidRoleException {
        return userLocationRoleService.edit(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) throws InvalidRoleException {
        userLocationRoleService.delete(id);
    }
}