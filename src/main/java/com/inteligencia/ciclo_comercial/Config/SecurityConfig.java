package com.inteligencia.ciclo_comercial.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/territorio/buscar-territorio","/territorio/lista-territorio", "/territorio/cadastro-territorio", "/cadastro-territorio", "/territorio/alterar-estado-territorio", "/revisao/atualizar-email", "/revisao/detalhes-territorio/{codigoTerritorio}", "/movimentacao/movimentacao-regional/{codigoTerritorio}", "/movimentacao/atualizar-regional", "/css/**", "/imagens/**", "/JS/**").permitAll()
                    .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()); // Desabilitar CSRF para fins de teste

        return http.build();
    }
}