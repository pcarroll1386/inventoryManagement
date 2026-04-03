package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.dto.UserLocationRoleRequest;
import com.pfc.inventorytrackerjpa.entities.Location;
import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.entities.User;
import com.pfc.inventorytrackerjpa.entities.UserLocationRole;
import com.pfc.inventorytrackerjpa.repositories.LocationRepository;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserLocationRoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserRepository;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidLocationException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/locationRoles")
public class UserLocationRoleController {

    private final UserLocationRoleRepository userLocationRoleRepo;

    private final UserRepository userRepo;

    private final LocationRepository locationRepo;

    private final RoleRepository roleRepo;

    public UserLocationRoleController(
            UserLocationRoleRepository userLocationRoleRepo,
            UserRepository userRepo,
            LocationRepository locationRepo,
            RoleRepository roleRepo) {
        this.userLocationRoleRepo = userLocationRoleRepo;
        this.userRepo = userRepo;
        this.locationRepo = locationRepo;
        this.roleRepo = roleRepo;
    }

    @GetMapping("/getAll")
    public List<UserLocationRole> getAll() {
        return userLocationRoleRepo.findAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<UserLocationRole> getByUser(@PathVariable("userId") long userId) {
        return userLocationRoleRepo.findAllByUserId(userId);
    }

    @GetMapping("/getByLocation/{locationId}")
    public List<UserLocationRole> getByLocation(@PathVariable("locationId") long locationId) {
        return userLocationRoleRepo.findAllByLocationId(locationId);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserLocationRole create(@Valid @RequestBody UserLocationRoleRequest request)
            throws InvalidDataException, InvalidUserException, InvalidLocationException, InvalidRoleException {
        User user = resolveUser(request);
        Location location = resolveLocation(request);
        Role role = resolveLocationRole(request);

        if (userLocationRoleRepo.existsByUserIdAndLocationIdAndRoleId(user.getId(), location.getId(), role.getId())) {
            throw new InvalidDataException("That location role assignment already exists.");
        }

        UserLocationRole assignment = new UserLocationRole();
        assignment.setUser(user);
        assignment.setLocation(location);
        assignment.setRole(role);

        ensureUserHasLocation(user, location);
        return userLocationRoleRepo.save(assignment);
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserLocationRole edit(@PathVariable("id") long id, @Valid @RequestBody UserLocationRoleRequest request)
            throws InvalidDataException, InvalidUserException, InvalidLocationException, InvalidRoleException {
        UserLocationRole current = userLocationRoleRepo.findById(id).orElse(null);
        if (current == null) {
            throw new InvalidDataException("Please provide a valid location role assignment.");
        }

        User user = resolveUser(request);
        Location location = resolveLocation(request);
        Role role = resolveLocationRole(request);

        current.setUser(user);
        current.setLocation(location);
        current.setRole(role);

        ensureUserHasLocation(user, location);
        return userLocationRoleRepo.save(current);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") long id) {
        UserLocationRole current = userLocationRoleRepo.findById(id).orElse(null);
        if (current == null) {
            return false;
        }

        long userId = current.getUser().getId();
        long locationId = current.getLocation().getId();
        User user = current.getUser();
        Location location = current.getLocation();

        userLocationRoleRepo.delete(current);

        if (userLocationRoleRepo.countByUserIdAndLocationId(userId, locationId) == 0) {
            user.removeLocation(location);
            userRepo.save(user);
        }

        return true;
    }

    private User resolveUser(UserLocationRoleRequest request) throws InvalidUserException {
        if (request.getUserId() == null) {
            throw new InvalidUserException("Please provide a user.");
        }
        long userId = request.getUserId();
        return userRepo.findById(userId)
                .orElseThrow(() -> new InvalidUserException("Invalid user id."));
    }

    private Location resolveLocation(UserLocationRoleRequest request) throws InvalidLocationException {
        if (request.getLocationId() == null) {
            throw new InvalidLocationException("Please provide a location.");
        }
        long locationId = request.getLocationId();
        return locationRepo.findById(locationId)
                .orElseThrow(() -> new InvalidLocationException("Invalid location id."));
    }

    private Role resolveLocationRole(UserLocationRoleRequest request) throws InvalidRoleException {
        if (request.getRoleId() == null) {
            throw new InvalidRoleException("Please provide a role.");
        }

        long roleId = request.getRoleId();
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new InvalidRoleException("Invalid role id."));

        if (role.getScope() != RoleScope.LOCATION) {
            throw new InvalidRoleException("Role must have LOCATION scope.");
        }

        return role;
    }

    private void ensureUserHasLocation(User user, Location location) {
        if (!user.getLocations().contains(location)) {
            user.addLocation(location);
            userRepo.save(user);
        }
    }
}