package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.dto.UserCreateRequest;
import com.pfc.inventorytrackerjpa.dto.UserUpdateRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.entities.User;
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

    public UserController(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
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
    public User createUser(@Valid @RequestBody UserCreateRequest request) throws InvalidRoleException {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setEmployeeIdentification(request.getEmployeeIdentification());
        user.setEnabled(request.getEnabled() == null || request.getEnabled());
        user.setRoles(resolveAppRoles(request.getAppRoleIds()));
        return userRepo.save(user);

    }

    @PatchMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User patchUser(@PathVariable("id") long id, @Valid @RequestBody UserUpdateRequest request)
            throws InvalidDataException, InvalidRoleException {
        User current = userRepo.findById(id).orElse(null);
        if (current == null) {
            throw new InvalidDataException("Please provide a valid user.");
        }

        if (request.getAppRoleIds() != null) {
            current.setRoles(resolveAppRoles(request.getAppRoleIds()));
        }
        if (request.getEmployeeIdentification() != null) {
            current.setEmployeeIdentification(request.getEmployeeIdentification());
        }
        if (request.getEnabled() != null) {
            current.setEnabled(request.getEnabled());
        }
        if (request.getName() != null) {
            current.setName(request.getName());
        }
        if (request.getPassword() != null) {
            current.setPassword(request.getPassword());
        }

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

    private Set<Role> resolveAppRoles(Set<Long> appRoleIds) throws InvalidRoleException {
        Set<Role> roles = new HashSet<>();
        if (appRoleIds == null) {
            return roles;
        }
        for (Long roleId : appRoleIds) {
            if (roleId == null) {
                continue;
            }
            Role resolvedRole = roleRepo.findById(roleId)
                    .orElseThrow(() -> new InvalidRoleException("Invalid role id."));
            if (resolvedRole.getScope() != RoleScope.APP) {
                throw new InvalidRoleException("Role '" + resolvedRole.getRoleName() + "' is not an app role.");
            }
            roles.add(resolvedRole);
        }
        return roles;
    }
}
