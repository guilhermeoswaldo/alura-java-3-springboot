package br.alura.medvollapi.domain.consulta.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.consulta.dto.DadosCancelamentoConsulta;
import br.alura.medvollapi.domain.consulta.entity.Consulta;
import br.alura.medvollapi.domain.consulta.repository.ConsultaRepository;
import br.alura.medvollapi.domain.medico.entity.Medico;
import br.alura.medvollapi.domain.medico.repository.MedicoRepository;
import br.alura.medvollapi.domain.paciente.repository.PacienteRepository;
import br.alura.medvollapi.infra.exception.ValidacaoException;

import jakarta.validation.Valid;


@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados) throws ValidacaoException {
        // Validação de integridade de dados
        this.validarDadosConsulta(dados);
        // Validações de Regra de Negócio
        
        var medico = this.escolherMedico(dados);
        var paciente = this.pacienteRepository.getReferenceById(
                dados.idPaciente()); // Usado para atribuir sem realizar alterações no objeto
        var consulta = new Consulta(medico, paciente, dados.data());
        this.consultaRepository.save(consulta);
    }

    public void cancelar(@Valid DadosCancelamentoConsulta dados) throws ValidacaoException {
        this.validarDadosCancelamento(dados);
        var consulta = this.consultaRepository.getReferenceById(dados.idConsulta());
        this.validarCancelamento(consulta);
        consulta.cancelar(dados.motivo());
    }

    private void validarDadosConsulta(DadosAgendamentoConsulta dados) throws ValidacaoException {
        if (!this.pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente %s informado não existe !".formatted(dados.idPaciente()));
        }

        if (dados.idMedico() != null && !this.medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do médico %s informado não existe !".formatted(dados.idMedico()));
        }

        if (dados.idMedico() == null && dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatório quando não informando médico");
        }
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return this.medicoRepository.getReferenceById(dados.idMedico());
        }
        return this.medicoRepository.buscarMedicoAleatorioDisponivelNaData(dados.especialidade(), dados.data());
    }

    private void validarDadosCancelamento(DadosCancelamentoConsulta dados) throws ValidacaoException {
        if (!this.consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta %s informado não existe !".formatted(dados.idConsulta()));
        }
    }

    private void validarCancelamento(Consulta consulta) throws ValidacaoException {
        if (LocalDateTime.now().isAfter(consulta.getData().minusHours(24L))) {
            throw new ValidacaoException("Uma consulta só pode ser cancelada com antecedência mínima de 24 horas");
        }
    }
}
