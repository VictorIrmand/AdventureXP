package org.example.adventurexp.config;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .securityContext(context -> context.requireExplicitSave(false))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(a -> a
                        // 🟢 FRONTEND MÅ ALTID LOADES
                        .requestMatchers(
                                "/", "/index.html",
                                "/signup", "/login",
                                "/home", "/homepage",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico"
                        ).permitAll()

                        // 🟢 Auth endpoints (login/signup/logout)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Admin mounts
                        .requestMatchers("/manage-activities", "/create-activity", "/update-activity", "/manage-users", "register-employee", "/update-user").hasRole("ADMIN")


                        // 🟡 API endpoints (beskyttede)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN","CUSTOMER")

                        // 🔒 ALT ANDET KRÆVER LOGIN
                        .anyRequest().authenticated()
                )

                // 🟢 Håndtér API-fejl – men IKKE på frontend-views
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((req, res, ex) -> {
                            if (req.getRequestURI().startsWith("/api/")) {
                                res.setStatus(401);
                                res.setContentType("application/json");
                                res.getWriter().write("{\"error\":\"unauthorized\"}");
                            } else {
                                // Hvis det er frontend-route (/home fx) → send bare index.html
                                req.getRequestDispatcher("/index.html").forward(req, res);
                            }
                        })
                )

                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
