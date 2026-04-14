package br.alura.medvollapi.domain.consulta.validacoes;


import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.consulta.repository.ConsultaRepository;

import jakarta.validation.ValidationException;


public class ValidadorMedicoComOutraConsultaMesmoHorario {

    private ConsultaRepository consultaRepository;

    public void validar(DadosAgendamentoConsulta dados) {
        var medicoPossuiConsultaMesmoHorario =
                this.consultaRepository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
        if (medicoPossuiConsultaMesmoHorario) {
            throw new ValidationException("Médico já possui outra consulta agendada no mesmo horário");
        }
    }

}
