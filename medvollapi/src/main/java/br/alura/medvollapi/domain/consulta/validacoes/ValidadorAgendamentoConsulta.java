package br.alura.medvollapi.domain.consulta.validacoes;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoConsulta {

    void validar(DadosAgendamentoConsulta dados);
}
