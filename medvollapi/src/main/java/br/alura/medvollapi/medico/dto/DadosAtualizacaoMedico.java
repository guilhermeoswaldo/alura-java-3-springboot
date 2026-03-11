package br.alura.medvollapi.medico.dto;


import br.alura.medvollapi.endereco.dto.DadosEndereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        @Valid DadosEndereco endereco) {}
