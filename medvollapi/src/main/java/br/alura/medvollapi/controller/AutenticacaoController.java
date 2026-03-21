package br.alura.medvollapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.alura.medvollapi.domain.usuario.dto.DadosAutenticacao;
import br.alura.medvollapi.infra.security.DadosTokenJWT;
import br.alura.medvollapi.infra.security.TokenService;
import br.alura.medvollapi.infra.security.UsuarioPrincipal;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var auth = authenticationManager.authenticate(authToken);

        var jwtToken = tokenService.gerarToken((UsuarioPrincipal) auth.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(jwtToken));
    }
}
