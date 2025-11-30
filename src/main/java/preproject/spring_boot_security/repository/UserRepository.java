package preproject.spring_boot_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import preproject.spring_boot_security.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}