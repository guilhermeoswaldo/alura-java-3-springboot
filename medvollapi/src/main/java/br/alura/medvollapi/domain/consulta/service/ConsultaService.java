package br.alura.medvollapi.domain.consulta.service;


import java.time.LocalDateTime;
import java.util.List;

import br.alura.medvollapi.domain.consulta.validacoes.ValidadorAgendamentoConsulta;
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

    @Autowired
    private List<ValidadorAgendamentoConsulta> validadores; // Identifica todas as listas que implementam a interface e injeta cada uma delas

    public void agendar(DadosAgendamentoConsulta dados) {
        // Validação de integridade de dados
        this.validarDadosConsulta(dados);
        // Validações de Regra de Negócio -> Design Pattern Strategy
        // Aplicação do S, O e D do SOLID:
        // S -> Single Responsibility Principle (cada classe de validação tem apenas uma responsabilidade)
        // O -> Open-Closed Principle (a service está aberta para novos validadores sem precisar alterar seu código)
        // D -> Dependency Inversion Principle (a service depende de uma abstração/interface e não de implementações concretas)
        validadores.forEach(v -> v.validar(dados));

        var medico = this.escolherMedico(dados);
        var paciente = this.pacienteRepository.getReferenceById(
                dados.idPaciente()); // Usado para atribuir sem realizar alterações no objeto
        var consulta = new Consulta(medico, paciente, dados.data());
        this.consultaRepository.save(consulta);
    }

    public void cancelar(@Valid DadosCancelamentoConsulta dados) {
        this.validarDadosCancelamento(dados);
        var consulta = this.consultaRepository.getReferenceById(dados.idConsulta());
        this.validarCancelamento(consulta);
        consulta.cancelar(dados.motivo());
    }

    private void validarDadosConsulta(DadosAgendamentoConsulta dados) {
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

    private void validarDadosCancelamento(DadosCancelamentoConsulta dados) {
        if (!this.consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta %s informado não existe !".formatted(dados.idConsulta()));
        }
    }

    private void validarCancelamento(Consulta consulta) {
        if (LocalDateTime.now().isAfter(consulta.getData().minusHours(24L))) {
            throw new ValidacaoException("Uma consulta só pode ser cancelada com antecedência mínima de 24 horas");
        }
    }
}
