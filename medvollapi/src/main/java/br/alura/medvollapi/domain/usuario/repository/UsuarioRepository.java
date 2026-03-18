package br.alura.medvollapi.domain.usuario.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.domain.usuario.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);
}
