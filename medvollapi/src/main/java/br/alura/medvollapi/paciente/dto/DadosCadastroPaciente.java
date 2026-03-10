package br.alura.medvollapi.paciente.dto;


import br.alura.medvollapi.endereco.dto.DadosCadastroEndereco;


public record DadosCadastroPaciente(String nome, String email, String telefone, String cpf,
                                    DadosCadastroEndereco endereco) {}
