package br.alura.medvollapi.domain.consulta.validacoes.cancelamento;


import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.alura.medvollapi.domain.consulta.dto.DadosCancelamentoConsulta;
import br.alura.medvollapi.domain.consulta.repository.ConsultaRepository;
import br.alura.medvollapi.infra.exception.ValidacaoException;


@Component("ValidadorHorarioAntecedenciaCancelamento")
public class ValidadorHorarioAntecedenciaCancelamento implements ValidadorCancelamentoConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = this.consultaRepository.getReferenceById(dados.idConsulta());
        var dataConsulta = consulta.getData();
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, dataConsulta).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Consulta só pode ser cancelada com antecedência mínima de 24 horas");
        }
    }
}
