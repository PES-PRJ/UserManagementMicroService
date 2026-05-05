package bt.edu.gcit.usermanagementmicroservice.dao;

import bt.edu.gcit.usermanagementmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Critical for Login: finds the user by their unique email
    User findByEmail(String email);
    User findByRegistrationToken(String registrationToken);
}