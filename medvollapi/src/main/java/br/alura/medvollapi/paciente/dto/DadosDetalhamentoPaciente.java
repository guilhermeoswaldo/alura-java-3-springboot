package br.alura.medvollapi.paciente.dto;


import br.alura.medvollapi.endereco.dto.DadosEndereco;
import br.alura.medvollapi.paciente.entity.Paciente;


public record DadosDetalhamentoPaciente(Long id, String nome, String email, String cpf, DadosEndereco endereco) {

    public DadosDetalhamentoPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf(),
                new DadosEndereco(paciente.getEndereco()));
    }
}
