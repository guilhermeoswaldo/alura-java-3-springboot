package br.alura.medvollapi.paciente.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.paciente.entity.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Page<Paciente> findAllByAtivo(Pageable paginacao, Boolean ativo);
}
