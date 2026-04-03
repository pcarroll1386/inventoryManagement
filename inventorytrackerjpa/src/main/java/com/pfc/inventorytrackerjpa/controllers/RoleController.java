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
    

}
