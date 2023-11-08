package com.example.authenticationya.service.seviceInterface;

import org.springframework.stereotype.Repository;
import com.example.authenticationya.dto.request.*;
import com.example.authenticationya.dto.response.*;

@Repository
public interface AuthenticationService {

    void registration(RegistrationDto registrationDto);

    JwtResponse authentication(LogInDto logInDto);

    boolean activateUser(String code);

}
