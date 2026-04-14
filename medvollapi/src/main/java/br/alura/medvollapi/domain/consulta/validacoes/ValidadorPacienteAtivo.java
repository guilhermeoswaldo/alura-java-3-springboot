package br.alura.medvollapi.domain.consulta.validacoes;


import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.paciente.repository.PacienteRepository;
import br.alura.medvollapi.infra.exception.ValidacaoException;


public class ValidadorPacienteAtivo {

    private PacienteRepository pacienteRepository;

    public void validar(DadosAgendamentoConsulta dados) throws ValidacaoException {
        var pacienteEstaAtivo = this.pacienteRepository.findAtivoById(dados.idPaciente());
        if (!pacienteEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser marcada com paciente excluído");
        }
    }
}
