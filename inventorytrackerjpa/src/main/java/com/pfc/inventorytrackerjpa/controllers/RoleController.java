package com.pfc.inventorytrackerjpa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;



@RestController
@RequestMapping("/roles")
public class RoleController {

   
    @Autowired
    RoleRepository roleRepo;

    @GetMapping("/getall")
    public List<Role> getall() {
        return roleRepo.findAll();
    }

    @GetMapping("/getById{id}")
    public Role getById(@PathVariable("id") long id){
        return roleRepo.findById(id).orElse(null);
    }

    @GetMapping("/getByRoleName")
    public Role getByRoleName(@PathVariable("roleName") String roleName) {
        return roleRepo.findByRoleName(roleName);
    }
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@RequestBody Role role) {
        return roleRepo.save(role);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Role editRole(@RequestBody Role role) throws InvalidRoleException, InvalidDataException {
        Role current = roleRepo.findById(role.getId()).orElse(null);
        if (current == null) {
            throw new InvalidRoleException("Please provide a valid role.");
        }
        current.setRoleName(role.getRoleName());
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
    

}
