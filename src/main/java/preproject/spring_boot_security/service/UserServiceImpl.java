package preproject.spring_boot_security.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import preproject.spring_boot_security.model.Role;
import preproject.spring_boot_security.model.User;
import preproject.spring_boot_security.repository.RoleRepository;
import preproject.spring_boot_security.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public void createUser(User user, List<Long> roleIds, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified");
        }
        Set<Role> roles = roleService.getRolesByIds(roleIds);
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, List<Long> roleIds, String rawPassword) {
        User existing =  getUserById(user.getId());
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setAge(user.getAge());
        existing.setEmail(user.getEmail());

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified");
        }
        Set<Role> roles = roleService.getRolesByIds(roleIds);
        existing.setRoles(roles);

        if (rawPassword != null && !rawPassword.isBlank()) {
            existing.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        }
        userRepository.save(existing);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email);
    }
}