package br.alura.medvollapi.domain.medico.dto;


import br.alura.medvollapi.domain.endereco.dto.DadosEndereco;
import br.alura.medvollapi.domain.medico.EspecialidadeMedicoEnum;
import br.alura.medvollapi.domain.medico.entity.Medico;


public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, String telefone,
                                      EspecialidadeMedicoEnum especialidade, DadosEndereco endereco) {

    public DadosDetalhamentoMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(),
                medico.getEspecialidade(), new DadosEndereco(medico.getEndereco()));
    }
}
