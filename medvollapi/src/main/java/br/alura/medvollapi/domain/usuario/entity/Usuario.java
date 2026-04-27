package br.alura.medvollapi.domain.usuario.entity;


import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
// A entidade não deve implementar diretamente o UserDetails, ele deve ter uma Wrapper intermediário para separar as responsabilidades e para que fosse protegido informações confidenciais do usuário ja que esse UserDetails fica trafegando no contexto de segurança do Spring.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_perfis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_permissao")
    private List<PerfilPermissao> perfilPermissoes;

}
