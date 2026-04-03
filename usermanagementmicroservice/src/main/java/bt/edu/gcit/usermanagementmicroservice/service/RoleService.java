package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.entity.Role;
import java.util.List;

public interface RoleService {
    Role findByName(String name);
    Role save(Role role);
    List<Role> findAll();
}