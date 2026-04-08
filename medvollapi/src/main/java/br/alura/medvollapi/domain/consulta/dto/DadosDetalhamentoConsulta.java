package br.alura.medvollapi.domain.consulta.dto;


import java.time.LocalDateTime;


public record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {}
