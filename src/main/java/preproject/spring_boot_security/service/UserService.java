package preproject.spring_boot_security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import preproject.spring_boot_security.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByEmail(String email);
    void createUser(User user, List<Long> roleIds, String rawPassword);
    void updateUser(User user, List<Long> roleIds, String rawPassword);
    void deleteUserById(Long id);
}