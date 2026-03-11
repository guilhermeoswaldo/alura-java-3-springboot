package br.alura.medvollapi.medico.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.medico.entity.Medico;


public interface MedicoRepository extends JpaRepository<Medico, Long> {}
