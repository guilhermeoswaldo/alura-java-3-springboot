package br.alura.medvollapi.domain.paciente.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.domain.paciente.entity.Paciente;
import org.springframework.data.jpa.repository.Query;


public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Page<Paciente> findAllByAtivo(Pageable paginacao, Boolean ativo);

    @Query("""
                    SELECT p.ativo
                    FROM Paciente p
                    WHERE p.id = :id
            """)
    Boolean findAtivoById(Long id);
}
