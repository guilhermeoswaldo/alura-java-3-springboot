package br.alura.medvollapi.domain.consulta.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.alura.medvollapi.domain.consulta.entity.Consulta;


public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

}
