package org.example.adventurexp.repository;

import org.example.adventurexp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // smart metode: søg på begge
    default Optional<User> findByUsernameOrEmail(String input) {
        Optional<User> user = findByUsername(input);
        if (user.isEmpty()) {
            user = findByEmail(input);
        }
        return user;
    }
}
