package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepo;

    public RoleController(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @GetMapping("/getall")
    public List<Role> getall() {
        return roleRepo.findAll();
    }

    @GetMapping("/getById/{id}")
    public Role getById(@PathVariable("id") long id){
        return roleRepo.findById(id).orElse(null);
    }

    @GetMapping("/getByRoleName/{roleName}")
    public Role getByRoleName(@PathVariable("roleName") String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    @GetMapping("/getByScope/{scope}")
    public List<Role> getByScope(@PathVariable("scope") RoleScope scope) {
        return roleRepo.findAllByScope(scope);
    }
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@RequestBody Role role) throws InvalidRoleException {
        validateRole(role);
        Role savedRole = roleRepo.save(role);
        if (savedRole == null) {
            throw new InvalidRoleException("Unable to save role.");
        }
        return savedRole;
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Role editRole(@RequestBody Role role) throws InvalidRoleException {
        Role current = roleRepo.findById(role.getId()).orElse(null);
        if (current == null) {
            throw new InvalidRoleException("Please provide a valid role.");
        }
        validateRole(role);
        current.setRoleName(role.getRoleName());
        current.setScope(role.getScope());
        return roleRepo.save(current);
    }

    @PostMapping("/delete/{id}")
    public boolean deleteRoleById(@PathVariable("id") long id) {
        Role role = roleRepo.findById(id).orElse(null);
        if (role != null ){
            roleRepo.delete(role);
            return true;
        }
        return false;
    }

    private void validateRole(Role role) throws InvalidRoleException {
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new InvalidRoleException("Please provide a valid role name.");
        }
        if (role.getScope() == null) {
            throw new InvalidRoleException("Please provide a role scope.");
        }
    }
    

}
