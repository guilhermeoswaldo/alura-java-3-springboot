package br.alura.medvollapi.medico.dto;


import br.alura.medvollapi.endereco.dto.DadosEndereco;
import br.alura.medvollapi.medico.EspecialidadeMedicoEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record DadosCadastroMedico(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "\\([1-9][0-9]\\) \\d{4,5}-\\d{4}")
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull
        EspecialidadeMedicoEnum especialidade,
        @NotNull
        @Valid
        DadosEndereco endereco) {}
