package br.alura.medvollapi.paciente.dto;


import br.alura.medvollapi.paciente.entity.Paciente;


public record DadosListagemPaciente(String nome, String email, String cpf) {

    public DadosListagemPaciente(Paciente paciente) {
        this(paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}
