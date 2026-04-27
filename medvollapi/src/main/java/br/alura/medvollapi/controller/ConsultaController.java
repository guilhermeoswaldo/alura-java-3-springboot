package br.alura.medvollapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.consulta.dto.DadosCancelamentoConsulta;
import br.alura.medvollapi.domain.consulta.dto.DadosDetalhamentoConsulta;
import br.alura.medvollapi.domain.consulta.service.ConsultaService;
import br.alura.medvollapi.infra.exception.ValidacaoException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados)
            throws ValidacaoException {
        var dto = this.consultaService.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cancelar")
    @Transactional
    public ResponseEntity<Void> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados)
            throws ValidacaoException {
        this.consultaService.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
