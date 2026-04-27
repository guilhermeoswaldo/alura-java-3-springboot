package br.alura.medvollapi.domain.consulta.validacoes.agendamento;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.paciente.repository.PacienteRepository;
import br.alura.medvollapi.infra.exception.ValidacaoException;


@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoConsulta {

    @Autowired
    private PacienteRepository pacienteRepository;

    public void validar(DadosAgendamentoConsulta dados) {
        var pacienteEstaAtivo = this.pacienteRepository.findAtivoById(dados.idPaciente());
        if (!pacienteEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser marcada com paciente excluído");
        }
    }
}
