package com.pfc.inventorytrackerjpa.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pfc.inventorytrackerjpa.entities.Role;
import com.pfc.inventorytrackerjpa.entities.RoleScope;
import com.pfc.inventorytrackerjpa.repositories.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Role> getByScope(RoleScope scope) {
        return roleRepo.findAllByScope(scope);
    }

    @Transactional(readOnly = true)
    public Role getById(long id) throws InvalidRoleException {
        return roleRepo.findById(id).orElseThrow(() -> new InvalidRoleException("Please provide a valid role id."));
    }

    @Transactional(readOnly = true)
    public Role getByRoleName(String roleName) throws InvalidRoleException {
        Role role = roleRepo.findByRoleName(roleName);
        if (role == null) {
            throw new InvalidRoleException("Please provide a valid role name.");
        }
        return role;
    }

    
}
