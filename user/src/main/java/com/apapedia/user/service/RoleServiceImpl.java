package com.apapedia.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.user.model.Role;
import com.apapedia.user.repository.RoleDb;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{
    
    @Autowired
    private RoleDb roleDb;

    @Override
    public List<Role> getAllList() {
        return roleDb.findAll();
    }

    @Override
    public Role getRoleByRoleName(String name){
        Optional<Role> role = roleDb.findByRole(name);
        if (role.isPresent()) {
            return role.get();
        }
        return null;
    }

    @Override
    public void addRole(String roleName){
        Role role = new Role();
        role.setRole(roleName);
        roleDb.save(role);
    }


}
