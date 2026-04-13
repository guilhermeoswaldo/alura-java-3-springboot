package br.alura.medvollapi.domain.medico.dto;


import br.alura.medvollapi.domain.endereco.dto.DadosEndereco;
import br.alura.medvollapi.domain.medico.EspecialidadeMedico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record DadosCadastroMedico(
        @NotBlank(message = "{nome.obrigatorio}")
        String nome,
        @NotBlank(message = "{email.obrigatorio}")
        @Email(message = "{email.invalido}")
        String email,
        @NotBlank(message = "{telefone.obrigatorio}")
        @Pattern(regexp = "\\([1-9][0-9]\\) \\d{4,5}-\\d{4}", message = "{telefone.invalido}")
        String telefone,
        @NotBlank(message = "{crm.obrigatorio}")
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull(message = "{especialidade.obrigatoria}")
        EspecialidadeMedico especialidade,
        @NotNull(message = "{endereco.obrigatorio}")
        @Valid
        DadosEndereco endereco) {}
