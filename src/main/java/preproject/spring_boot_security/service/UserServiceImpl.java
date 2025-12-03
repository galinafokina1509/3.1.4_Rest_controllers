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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public User createUser(User user) {
        String rawPassword = user.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }
        Set<Role> roles = resolveRolesFromUser(user);
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing =  getUserById(id);
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setAge(user.getAge());
        existing.setEmail(user.getEmail());

        Set<Role> roles = resolveRolesFromUser(user);
        existing.setRoles(roles);

        String rawPassword = user.getPassword();
        if (rawPassword != null && !rawPassword.isBlank()) {
            existing.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        }
        return userRepository.save(existing);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email);
    }

    private Set<Role> resolveRolesFromUser(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified");
        }
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).toList();
        return roleService.getRolesByIds(roleIds);
    }
}