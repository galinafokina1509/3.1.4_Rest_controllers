package preproject.spring_boot_security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import preproject.spring_boot_security.model.Role;
import preproject.spring_boot_security.model.User;
import preproject.spring_boot_security.service.RoleService;
import preproject.spring_boot_security.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        List<Long> roleIds = null;
        if (user.getRoles() != null) {
            roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        }
        userService.createUser(user, roleIds, user.getPassword());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        List<Long> roleIds = null;
        if (user.getRoles() != null) {
            roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        }
        userService.updateUser(user, roleIds, user.getPassword());
        User updatedUser = userService.getUserById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}