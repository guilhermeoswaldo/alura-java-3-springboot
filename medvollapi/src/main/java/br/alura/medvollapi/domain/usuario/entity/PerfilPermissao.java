package br.alura.medvollapi.domain.usuario.entity;

public enum PerfilPermissao {
    ROLE_USER,
    ROLE_ADMIN;

    // Constantes para serem usadas nas anotações do Spring Security
    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
}