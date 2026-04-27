package br.alura.medvollapi.domain.consulta.validacoes.agendamento;


import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.infra.exception.ValidacaoException;


// Possibilidade de renomeação do Bean. Para o Java ter duas classes com mesmo nome em pacotes diferentes não tem problema, porém para o spring não é possível ter dois beans com o mesmo nome e dessa forma podemos renomea-los
@Component("ValidadorHorarioAntecedenciaAgendamento")
public class ValidadorHorarioAntecedenciaAgendamento implements ValidadorAgendamentoConsulta {

    public void validar(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
        }
    }
}
