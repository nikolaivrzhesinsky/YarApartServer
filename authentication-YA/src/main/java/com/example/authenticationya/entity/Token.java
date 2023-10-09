package com.example.authenticationya.entity;

import com.example.authenticationya.entity.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    public String token;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public TokenType tokenType = TokenType.BEARER;

    @Column(name = "revoked")
    public boolean revoked;
    @Column(name="expired")
    public boolean expired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User user;


}
