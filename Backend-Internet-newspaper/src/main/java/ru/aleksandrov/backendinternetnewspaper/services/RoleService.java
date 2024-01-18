package ru.aleksandrov.backendinternetnewspaper.services;

import ru.aleksandrov.backendinternetnewspaper.models.ERole;
import ru.aleksandrov.backendinternetnewspaper.models.Role;
import ru.aleksandrov.backendinternetnewspaper.models.User;

public interface RoleService {
    Role getRoleByName(ERole role);
    void setDefaultRole(User user);
}
