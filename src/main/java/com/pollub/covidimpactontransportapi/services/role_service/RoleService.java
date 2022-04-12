package com.pollub.covidimpactontransportapi.services.role_service;

import com.pollub.covidimpactontransportapi.entities.Role;
import com.pollub.covidimpactontransportapi.repositories.IRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
