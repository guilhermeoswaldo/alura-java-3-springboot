package br.alura.medvollapi.domain.consulta.validacoes;


import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.medico.repository.MedicoRepository;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoConsulta {

    @Autowired
    private MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados) {
        // Id médico é opcional
        if (dados.idMedico() == null) {
            return;
        }

        var medicoEstaAtivo = this.medicoRepository.findAtivoById(dados.idMedico());
        if (!medicoEstaAtivo) {
            throw new ValidationException("Consulta não pode ser agendada com médico excluído");
        }
    }
}
