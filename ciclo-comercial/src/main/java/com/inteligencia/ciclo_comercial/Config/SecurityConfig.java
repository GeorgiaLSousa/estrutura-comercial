package com.inteligencia.ciclo_comercial.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/dashboard", "/redefinir-senha", "/territorio/**", "/revisao/**", "/movimentacao/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_ANALISTA")
                    .requestMatchers("/usuarios/**").hasAuthority("ROLE_ADMINISTRADOR")
                    .requestMatchers("/css/**", "/imagens/**", "/JS/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("emailUsuario")
                .passwordParameter("senha")
                .permitAll()
                .failureHandler(customAuthenticationFailureHandler)
                .successHandler(customAuthenticationSuccessHandler)
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}