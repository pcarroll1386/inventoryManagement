package com.pfc.inventorytrackerjpa.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pfc.inventorytrackerjpa.dto.UserCreateRequest;
import com.pfc.inventorytrackerjpa.dto.UserUpdateRequest;
import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.entities.User;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;
import com.pfc.inventorytrackerjpa.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    public UserService(
        UserRepository userRepo, 
        RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(long id) throws InvalidUserException {
        return userRepo.findById(id).orElseThrow(() ->  new InvalidUserException("Please provide a valid user id."));
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) throws InvalidUserException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new InvalidUserException("Please provide a valid username.");
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserCreateRequest request) throws InvalidRoleException, InvalidUserException {
        if (usernameExists(request.getUsername())) {
            throw new InvalidUserException("That username is already taken.");
        }
        Role appRole = resolveAppRole(request.getAppRoleId());
        User user = new User(request.getUsername(), request.getPassword(), request.getName(), request.getEmployeeIdentification(), request.getEnabled() == null || request.getEnabled());
        user.setAppRole(appRole);
        return userRepo.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateUser(long id, UserUpdateRequest request) throws InvalidDataException, InvalidRoleException, InvalidUserException {
        User current = userRepo.findById(id).orElseThrow(() -> new InvalidUserException("Please provide a valid user"));
        if (request.getEmployeeIdentification() != null && !request.getEmployeeIdentification().equals(current.getEmployeeIdentification())
                && employeeIdentificationExists(request.getEmployeeIdentification())) {
            throw new InvalidDataException("That employee identification is already taken.");
        }
        if (request.getAppRoleId() != null) {
            Role appRole = resolveAppRole(request.getAppRoleId());
            current.setAppRole(appRole);
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

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(long id) throws InvalidUserException {
        User current = userRepo.findById(id).orElseThrow(() -> new InvalidUserException("Please provide a valid user"));
        userRepo.delete(current);
    }


    private boolean usernameExists(String username) {
        return userRepo.findByUsername(username) != null;
    }

    private boolean employeeIdentificationExists(String employeeIdentification) {
        return userRepo.findAll().stream()
                .anyMatch(u -> u.getEmployeeIdentification() != null && u.getEmployeeIdentification().equals(employeeIdentification));
    }

    private Role resolveAppRole(Long appRoleId) throws InvalidRoleException {
        if (appRoleId == null) {
            throw new InvalidRoleException("App role id is required.");
        }
        Role role = roleRepo.findById(appRoleId)
                .orElseThrow(() -> new InvalidRoleException("Invalid role id."));
        if (role.getScope() != RoleScope.APP) {
            throw new InvalidRoleException("Role '" + role.getRoleName() + "' is not an app role.");
        }
        return role;
    }
}