package br.alura.medvollapi.domain.consulta.validacoes.agendamento;


import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.infra.exception.ValidacaoException;


@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoConsulta {

    public void validar(DadosAgendamentoConsulta dados) throws ValidacaoException {
        var dataConsulta = dados.data();

        var isDomingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var isHoraAntesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var isHoraDepoisDoFechamentoDaClinica = dataConsulta.getHour() > 18;
        if (isDomingo || isHoraAntesDaAberturaDaClinica || isHoraDepoisDoFechamentoDaClinica) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
        }
    }
}
