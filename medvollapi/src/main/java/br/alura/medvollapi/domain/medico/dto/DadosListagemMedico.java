package br.alura.medvollapi.domain.medico.dto;


import br.alura.medvollapi.domain.medico.EspecialidadeMedico;
import br.alura.medvollapi.domain.medico.entity.Medico;


public record DadosListagemMedico(Long id, String nome, String email, String crm,
                                  EspecialidadeMedico especialidade) {

    public DadosListagemMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
