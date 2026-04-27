package br.alura.medvollapi.domain.consulta.validacoes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.consulta.repository.ConsultaRepository;
import br.alura.medvollapi.infra.exception.ValidacaoException;

@Component
public class ValidadorMedicoComOutraConsultaMesmoHorario implements ValidadorAgendamentoConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    public void validar(DadosAgendamentoConsulta dados) {
        var medicoPossuiConsultaMesmoHorario =
                this.consultaRepository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
        if (medicoPossuiConsultaMesmoHorario) {
            throw new ValidacaoException("Médico já possui outra consulta agendada no mesmo horário");
        }
    }

}
