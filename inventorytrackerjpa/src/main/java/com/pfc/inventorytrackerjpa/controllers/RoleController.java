package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.RoleService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.pfc.inventorytrackerjpa.entities.Role;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/getall")
    public List<Role> getall() {
        return roleService.getAll();
    }

    @GetMapping("/getById/{id}")
    public Role getById(@PathVariable("id") long id) throws InvalidRoleException{
        return roleService.getById(id);
    }

    @GetMapping("/getByRoleName/{roleName}")
    public Role getByRoleName(@PathVariable("roleName") String roleName) throws InvalidRoleException {
        return roleService.getByRoleName(roleName);
    }

    @GetMapping("/getByScope/{scope}")
    public List<Role> getByScope(@PathVariable("scope") RoleScope scope) {
        return roleService.getByScope(scope);

    }


}
