package br.alura.medvollapi.domain.medico.repository;


import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.alura.medvollapi.domain.medico.EspecialidadeMedico;
import br.alura.medvollapi.domain.medico.entity.Medico;


public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivo(Pageable paginacao, Boolean ativo);

    @Query("""
            SELECT m FROM Medico m
            WHERE
                m.ativo = true
            AND
                m.especialidade = :especialidade
            AND
                m.id NOT IN(
                    SELECT c.medico.id FROM Consulta c
                    WHERE
                        c.data = :data
                    AND
                        c.motivoCancelamento IS NULL
                )
            ORDER BY rand()
            LIMIT 1
            """)
    Medico buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico especialidade, LocalDateTime data);

    @Query("""
            SELECT m.ativo
            FROM Medico m
            WHERE m.id = :id
            """)
    Boolean findAtivoById(Long id);
}
