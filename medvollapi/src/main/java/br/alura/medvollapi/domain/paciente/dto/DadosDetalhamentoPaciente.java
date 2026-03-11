package br.alura.medvollapi.domain.paciente.dto;


import br.alura.medvollapi.domain.endereco.dto.DadosEndereco;
import br.alura.medvollapi.domain.paciente.entity.Paciente;


public record DadosDetalhamentoPaciente(Long id, String nome, String email, String cpf, DadosEndereco endereco) {

    public DadosDetalhamentoPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf(),
                new DadosEndereco(paciente.getEndereco()));
    }
}
