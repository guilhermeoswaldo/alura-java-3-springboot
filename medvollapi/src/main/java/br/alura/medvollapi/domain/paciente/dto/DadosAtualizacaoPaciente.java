package br.alura.medvollapi.domain.paciente.dto;


import br.alura.medvollapi.domain.endereco.dto.DadosEndereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record DadosAtualizacaoPaciente(
        @NotNull(message = "{id.obrigatorio}")
        Long id,
        String nome,
        String telefone,
        @Valid DadosEndereco endereco) {}
