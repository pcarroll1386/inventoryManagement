package com.pfc.inventorytrackerjpa.services;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLocationRoleService {

    private final UserLocationRoleRepository userLocationRoleRepo;
    private final UserRepository userRepo;
    private final LocationRepository locationRepo;
    private final RoleRepository roleRepo;

    public UserLocationRoleService(
            UserLocationRoleRepository userLocationRoleRepo,
            UserRepository userRepo,
            LocationRepository locationRepo,
            RoleRepository roleRepo) {
        this.userLocationRoleRepo = userLocationRoleRepo;
        this.userRepo = userRepo;
        this.locationRepo = locationRepo;
        this.roleRepo = roleRepo;
    }

    @Transactional(readOnly = true)
    public List<UserLocationRole> getAll() {
        return userLocationRoleRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserLocationRole> getByUser(long userId) {
        return userLocationRoleRepo.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserLocationRole> getByLocation(long locationId) {
        return userLocationRoleRepo.findAllByLocationId(locationId);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLocationRole create(UserLocationRoleRequest request)
            throws InvalidDataException, InvalidUserException, InvalidLocationException, InvalidRoleException {
        User user = resolveUser(request);
        Location location = resolveLocation(request);
        Role role = resolveLocationRole(request);

        if (userLocationRoleRepo.existsByUserIdAndLocationIdAndRoleId(user.getId(), location.getId(), role.getId())) {
            throw new InvalidDataException("That location role assignment already exists.");
        }

        UserLocationRole assignment = new UserLocationRole(user, location, role);

        ensureUserHasLocation(user, location);
        return userLocationRoleRepo.save(assignment);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLocationRole edit(long id, UserLocationRoleRequest request)
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

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(long id) {
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