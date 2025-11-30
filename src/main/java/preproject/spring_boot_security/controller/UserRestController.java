package preproject.spring_boot_security.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import preproject.spring_boot_security.model.User;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }
}