package br.alura.medvollapi.domain.consulta.dto;


import java.time.LocalDateTime;

import br.alura.medvollapi.domain.consulta.entity.Consulta;


public record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {

    public DadosDetalhamentoConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData());
    }

}
