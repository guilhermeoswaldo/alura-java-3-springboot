package br.alura.medvollapi.medico.dto;


import br.alura.medvollapi.medico.EspecialidadeMedicoEnum;
import br.alura.medvollapi.medico.entity.Medico;


public record DadosListagemMedico(String nome, String email, String crm, EspecialidadeMedicoEnum especialidade) {

    public DadosListagemMedico(Medico medico) {
        this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
