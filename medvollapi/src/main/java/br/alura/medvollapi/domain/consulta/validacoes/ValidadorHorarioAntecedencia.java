package br.alura.medvollapi.domain.consulta.validacoes;


import java.time.Duration;
import java.time.LocalDateTime;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.infra.exception.ValidacaoException;


public class ValidadorHorarioAntecedencia {

    public void validar(DadosAgendamentoConsulta dados) throws ValidacaoException {
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
        }
    }
}
