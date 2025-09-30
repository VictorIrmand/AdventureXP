package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(()->{
                    return new NoSuchElementException("Fail to find user!");
                });
    }
}
