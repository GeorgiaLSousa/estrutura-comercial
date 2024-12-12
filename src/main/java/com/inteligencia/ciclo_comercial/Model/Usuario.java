package com.inteligencia.ciclo_comercial.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_ciclo_comercial_user", schema = "dbo", catalog = "db_inteligenciacomercial")
public class Usuario implements UserDetails{

    @Id
    @Column(name = "Email")
    private String emailUsuario;

    @Column(name = "Name")
    private String nome;

    @Column(name = "[User]")
    private String userId;

    @Column(name = "Password")
    private String senha;

    @Column(name = "UserGroup")
    private String grupo;

    @Column(name = "isAtivo")
    private Boolean ativo = true; // Define o campo ativo como true por padrão

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + grupo));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return emailUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    public String getUserNome() {
        return nome;
    }

}