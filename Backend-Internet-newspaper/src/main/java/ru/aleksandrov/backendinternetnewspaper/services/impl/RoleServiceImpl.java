package ru.aleksandrov.backendinternetnewspaper.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.ERole;
import ru.aleksandrov.backendinternetnewspaper.models.Role;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.RoleRepository;
import ru.aleksandrov.backendinternetnewspaper.services.RoleService;


import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(ERole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> {
                    log.error("Role with name = " + role.name() + ": Not Found");
                    return new EntityNotFoundException("Role with name =with name = " + role.name() + ": Not Found");
                });
    }

    @Override
    public void setDefaultRole(User user) {
        Role role = getRoleByName(ERole.ROLE_USER);
        user.setRole(role);
    }
}
