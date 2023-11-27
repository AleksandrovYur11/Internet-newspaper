package ru.aleksandrov.backendinternetnewspaper.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.models.ERole;
import ru.aleksandrov.backendinternetnewspaper.models.Role;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.repositories.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(ERole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> {
                    log.error("Role not found");
                    return new EntityNotFoundException("Role not found");
                });
    }

    public void setDefaultRole(User user) {
        Set<Role> role = new HashSet<>();
        role.add(findByName(ERole.ROLE_USER));
        user.setRoles(role);
    }
}
