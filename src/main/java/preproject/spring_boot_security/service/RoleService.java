package preproject.spring_boot_security.service;

import preproject.spring_boot_security.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role findByName(String name);
    Set<Role> getRolesByIds(List<Long> ids);
}