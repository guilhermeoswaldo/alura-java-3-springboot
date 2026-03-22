package br.alura.medvollapi.infra.security.model;


import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.alura.medvollapi.domain.usuario.entity.Usuario;


public record SecurityUsuario(Usuario usuario) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public @Nullable String getPassword() {
        return this.usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return this.usuario.getLogin();
    }

    public Long getIdUsuario() {
        return this.usuario.getId();
    }
}
