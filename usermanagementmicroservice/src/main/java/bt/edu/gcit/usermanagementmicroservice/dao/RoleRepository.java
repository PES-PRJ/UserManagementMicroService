package bt.edu.gcit.usermanagementmicroservice.dao;

import bt.edu.gcit.usermanagementmicroservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}