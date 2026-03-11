package br.alura.medvollapi.paciente.dto;


import br.alura.medvollapi.endereco.dto.DadosEndereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        @Valid DadosEndereco endereco) {}
