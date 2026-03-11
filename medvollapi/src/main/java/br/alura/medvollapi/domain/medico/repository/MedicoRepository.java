package br.alura.medvollapi.domain.medico.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.domain.medico.entity.Medico;


public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivo(Pageable paginacao, Boolean ativo);
}
