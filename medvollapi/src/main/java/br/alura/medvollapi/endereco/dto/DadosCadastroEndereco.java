package br.alura.medvollapi.endereco.dto;


public record DadosCadastroEndereco(String logradouro, String bairro, String cidade, String uf, String complemento,
                                    String numero) {}
