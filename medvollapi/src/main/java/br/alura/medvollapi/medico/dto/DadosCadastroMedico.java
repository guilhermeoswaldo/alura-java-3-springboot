package br.alura.medvollapi.medico.dto;


import br.alura.medvollapi.endereco.dto.DadosCadastroEndereco;
import br.alura.medvollapi.medico.EspecialidadeMedicoEnum;


public record DadosCadastroMedico(String nome, String email, String crm, EspecialidadeMedicoEnum especialidade,
                                  DadosCadastroEndereco endereco) {}
