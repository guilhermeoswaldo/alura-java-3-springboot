package br.alura.medvollapi.domain.endereco.dto;


import br.alura.medvollapi.domain.endereco.entity.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record DadosEndereco(
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String cep,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        String complemento,
        String numero) {

        public DadosEndereco(Endereco endereco) {
                this(endereco.getLogradouro(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(),
                        endereco.getUf(), endereco.getComplemento(), endereco.getNumero());
        }
}
