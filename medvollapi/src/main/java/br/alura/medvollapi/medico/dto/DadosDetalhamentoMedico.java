package br.alura.medvollapi.medico.dto;


import br.alura.medvollapi.endereco.dto.DadosEndereco;
import br.alura.medvollapi.medico.EspecialidadeMedicoEnum;
import br.alura.medvollapi.medico.entity.Medico;


public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, String telefone,
                                      EspecialidadeMedicoEnum especialidade, DadosEndereco endereco) {

    public DadosDetalhamentoMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(),
                medico.getEspecialidade(), new DadosEndereco(medico.getEndereco()));
    }
}
