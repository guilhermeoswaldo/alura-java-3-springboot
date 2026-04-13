package br.alura.medvollapi.domain.consulta.dto;


import java.time.LocalDateTime;

import br.alura.medvollapi.domain.medico.EspecialidadeMedico;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;


public record DadosAgendamentoConsulta(
        Long idMedico,
        @NotNull
        Long idPaciente,
        EspecialidadeMedico especialidade,
        @NotNull
        @Future
        LocalDateTime data) {}
