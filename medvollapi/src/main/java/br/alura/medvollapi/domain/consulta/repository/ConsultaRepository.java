package br.alura.medvollapi.domain.consulta.repository;


import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.domain.consulta.entity.Consulta;


public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(Long idMedico, LocalDateTime data);

    boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime primeiroHorario,
            LocalDateTime ultimoHorario);
}
