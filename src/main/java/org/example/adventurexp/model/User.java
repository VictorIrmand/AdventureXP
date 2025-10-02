package org.example.adventurexp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "passwordHash")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String firstName;

    @Setter
    @Column(nullable = false)
    private String lastName;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Column(nullable = false)
    private String email;

    @Getter(AccessLevel.NONE)
    @Column(nullable = false)
    private String passwordHash;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    public String getPasswordHash() {
        return passwordHash;
    }

    public void changePasswordHash(String password) {
        this.passwordHash = password;
    }
}
