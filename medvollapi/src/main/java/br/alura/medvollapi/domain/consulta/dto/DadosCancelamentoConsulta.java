package br.alura.medvollapi.domain.consulta.dto;


import br.alura.medvollapi.domain.consulta.MotivoCancelamentoEnum;

import jakarta.validation.constraints.NotNull;


public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,
        @NotNull
        MotivoCancelamentoEnum motivo) {}
