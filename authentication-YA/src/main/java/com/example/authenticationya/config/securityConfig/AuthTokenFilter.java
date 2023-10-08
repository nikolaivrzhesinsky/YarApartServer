package com.example.authenticationya.config.securityConfig;


import com.example.authenticationya.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
//@Component
//@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CustomUserDetails customUserDetails;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> (!t.isExpired()) && (!t.isRevoked()))
                    .orElse(false);

            if (jwt != null && jwtGenerator.validateToken(jwt) && isTokenValid) {
                String userName = jwtGenerator.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = customUserDetails.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                UserDetails userDetailsTest =
                        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                log.info(userDetailsTest.getUsername() + userDetailsTest.getAuthorities());
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getLocalizedMessage());
        }

        filterChain.doFilter(request, response);

    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }


}
