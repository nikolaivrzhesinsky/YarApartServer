package com.example.authenticationya.service.serviceImpl;

import com.example.authenticationya.config.securityConfig.AuthTokenFilter;
import com.example.authenticationya.config.securityConfig.JwtGenerator;
import com.example.authenticationya.dto.request.LogInDto;
import com.example.authenticationya.dto.request.RegistrationDto;
import com.example.authenticationya.dto.response.JwtResponse;
import com.example.authenticationya.entity.Role;
import com.example.authenticationya.entity.Token;
import com.example.authenticationya.entity.User;
import com.example.authenticationya.entity.enums.Erole;
import com.example.authenticationya.entity.enums.TokenType;
import com.example.authenticationya.exception.AlreadyExistException;
import com.example.authenticationya.repository.RoleRepository;
import com.example.authenticationya.repository.TokenRepository;
import com.example.authenticationya.repository.UserRepository;
import com.example.authenticationya.service.seviceInterface.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final TokenRepository tokenRepository;
    private final AuthTokenFilter authTokenFilter;

    @Transactional
    @Override
    public void registration(RegistrationDto registrationDto) {

        //Checking for present
        log.info("Start registr");
        if (userRepository.existsByUserName(registrationDto.getUserName())
                || userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new AlreadyExistException("User already exist");
        }

        //create new persistent user
        User createUser = new User();
        createUser.setUserName(registrationDto.getUserName());
        createUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        createUser.setEmail(registrationDto.getEmail());
        Set<Role> newRoles = new HashSet<>();
        Role userRole = roleRepository.findByRole(Erole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        newRoles.add(userRole);
        createUser.setRoles(newRoles);

        var savedUser = userRepository.save(createUser);

        //MailSender
        //sendConfirmMail(savedUser);
        log.info("End registr");
    }

    @Override
    public JwtResponse authentication(LogInDto logInDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInDto.getUserName(),
                                                        logInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateToken(authentication);

        var user = userRepository.findByUserName(logInDto.getUserName())
                .orElseThrow();
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);

        return new JwtResponse(jwt);
    }

    private void saveUserToken(User user, String jwtToken) {

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {

        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

//    private void sendConfirmMail(User user) {
//
//        String message = String.format(
//                "Hello, %s! \n" +
//                        "Welcome to YarApart. Please, visit next link: http://localhost:8080/auth/activate/%s",
//                user.getUserName(),
//                user.getActivationCode()
//        );
//        mailSender.sendEmail(user.getEmail(), "Activation code", message);
//        log.info("Mail was sent");
//    }

    @Transactional
    public boolean activateUser(String code) {
        log.info("Activate block is working");
        User user = userRepository.findByActivationCode(code)
                .orElseThrow(() -> new IllegalArgumentException("activation code is not valid"));

        user.setActivationCode("none");
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

}
