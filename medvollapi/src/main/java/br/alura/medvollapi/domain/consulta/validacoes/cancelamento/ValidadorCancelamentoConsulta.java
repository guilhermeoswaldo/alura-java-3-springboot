package br.alura.medvollapi.domain.consulta.validacoes.cancelamento;


import br.alura.medvollapi.domain.consulta.dto.DadosCancelamentoConsulta;


public interface ValidadorCancelamentoConsulta {

    void validar(DadosCancelamentoConsulta dados);
}
