package com.example.gateway.config;
import com.example.gateway.filter.JwtTokenAuthenticationFilter;
import com.example.gateway.filter.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/test/**").hasAnyRole("ROLE_ADMIN","ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }





}
