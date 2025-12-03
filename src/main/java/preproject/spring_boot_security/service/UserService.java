package preproject.spring_boot_security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import preproject.spring_boot_security.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByEmail(String email);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
}