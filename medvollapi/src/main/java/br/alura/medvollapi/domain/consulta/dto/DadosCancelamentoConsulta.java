package br.alura.medvollapi.domain.consulta.dto;


import br.alura.medvollapi.domain.consulta.MotivoCancelamento;

import jakarta.validation.constraints.NotNull;


public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,
        @NotNull
        MotivoCancelamento motivo) {}
