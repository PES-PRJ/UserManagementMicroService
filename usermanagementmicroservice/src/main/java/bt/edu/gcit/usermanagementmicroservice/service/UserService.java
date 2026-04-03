package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    User update(Long id, User user);
}