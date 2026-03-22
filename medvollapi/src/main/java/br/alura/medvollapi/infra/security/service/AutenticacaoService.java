package br.alura.medvollapi.infra.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.alura.medvollapi.domain.usuario.repository.UsuarioRepository;
import br.alura.medvollapi.infra.security.model.SecurityUsuario;


@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username).map(SecurityUsuario::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + username));
    }
}
