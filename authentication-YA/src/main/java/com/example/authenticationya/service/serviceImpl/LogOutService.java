package com.example.authenticationya.service.serviceImpl;


import com.example.authenticationya.config.securityConfig.AuthTokenFilter;
import com.example.authenticationya.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
public class LogOutService implements LogoutHandler {

    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String jwtToken = authTokenFilter.parseJwt(request);
        var storedToken = tokenRepository.findByToken(jwtToken)
                .orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);

            SecurityContextHolder.clearContext();
        }

        log.info("LogOut complete");
    }
}
