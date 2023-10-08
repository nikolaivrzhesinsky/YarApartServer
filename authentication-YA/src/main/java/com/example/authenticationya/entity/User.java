package com.example.authenticationya.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name="date-of-created")
    private LocalDateTime dateOfCreated;

    @Column(name="activation-code")
    private String activationCode;
    @Column(name = "is-active")
    private boolean active;

    @PrePersist
    private void onCreate() {
        dateOfCreated = LocalDateTime.now();

        activationCode = UUID.randomUUID().toString();
        active = true;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users-roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    //    @Column(name = "tokens")
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Token> tokenList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
