package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.ERole;
import ru.aleksandrov.backendinternetnewspaper.model.Role;
import ru.aleksandrov.backendinternetnewspaper.model.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(ERole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> {
                    log.error("Role with name = " + role.name() + ": Not Found");
                    return new EntityNotFoundException("Role with name =with name = " + role.name() + ": Not Found");
                });
    }

    public void setDefaultRole(User user) {
        Role role = getRoleByName(ERole.ROLE_USER);
        user.setRole(role);
    }
}
