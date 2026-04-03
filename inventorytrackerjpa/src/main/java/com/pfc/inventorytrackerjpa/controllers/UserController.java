package com.pfc.inventorytrackerjpa.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.entities.User;
import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserRepository;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final LocationRepository locationRepo;

    public UserController(UserRepository userRepo, RoleRepository roleRepo, LocationRepository locationRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.locationRepo = locationRepo;
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public User getById(@PathVariable("id") long id) {
        return userRepo.findById(id).orElse(null);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) throws InvalidRoleException {
        user.setRoles(resolveAppRoles(user));
        user.setLocations(resolveLocations(user));
        return userRepo.save(user);

    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User editUser(@RequestBody User user) throws InvalidDataException, InvalidRoleException {
        User current = userRepo.findById(user.getId()).orElse(null);
        if (current == null) {
            throw new InvalidDataException("Please provide a valid user.");
        }
        current.setRoles(resolveAppRoles(user));
        current.setLocations(resolveLocations(user));
        current.setEmployeeIdentification(user.getEmployeeIdentification());
        current.setUsername(user.getUsername());
        current.setEnabled(user.isEnabled());
        current.setName(user.getName());
        current.setPassword(user.getPassword());
        return userRepo.save(current);
    }

    // When auth gets implimented this needs to be tied down to Super Auth user. use disable user instead.
    @DeleteMapping("/delete/{id}")
    public boolean deleteUserById(@PathVariable("id") long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            userRepo.delete(user);
            return true;
        }
        return false;
    }


    @PutMapping(value = "/toggleUserEnabled/{id}")
    public boolean toggleUserEnabled(@PathVariable("id") long id) throws InvalidUserException {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new InvalidUserException("Invalid user id.");
        }
        user.setEnabled(!user.isEnabled());
        userRepo.save(user);
        return user.isEnabled();
    }

    private Set<Role> resolveAppRoles(User user) throws InvalidRoleException {
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() == null) {
            return roles;
        }
        for (Role role : user.getRoles()) {
            if (role == null) {
                continue;
            }
            Role resolvedRole = roleRepo.findById(role.getId())
                    .orElseThrow(() -> new InvalidRoleException("Invalid role id."));
            if (resolvedRole.getScope() != RoleScope.APP) {
                throw new InvalidRoleException("Role '" + resolvedRole.getRoleName() + "' is not an app role.");
            }
            roles.add(resolvedRole);
        }
        return roles;
    }

    private Set<Location> resolveLocations(User user) {
        Set<Location> locations = new HashSet<>();
        if (user.getLocations() == null) {
            return locations;
        }
        for (Location location : user.getLocations()) {
            if (location == null) {
                continue;
            }
            locationRepo.findById(location.getId()).ifPresent(locations::add);
        }
        return locations;
    }
}
