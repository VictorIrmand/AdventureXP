package org.example.adventurexp.service;


import lombok.RequiredArgsConstructor;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Prøv at finde bruger enten via username eller email
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Bruger ikke fundet: " + usernameOrEmail));

        // Returnér en Spring Security UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // gemmer altid username som "principal"
                .password(user.getPasswordHash())     // password SKAL være hash’et (fx BCrypt)
                .roles(user.getRole().name().replace("ROLE_", "")) // fx "USER" eller "ADMIN"
                .build();
    }

}
