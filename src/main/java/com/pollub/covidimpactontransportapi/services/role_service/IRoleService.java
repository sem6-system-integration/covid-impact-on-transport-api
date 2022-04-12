package com.pollub.covidimpactontransportapi.services.role_service;

import com.pollub.covidimpactontransportapi.entities.Role;

public interface IRoleService {
    Role getRoleByName(String name);
}
