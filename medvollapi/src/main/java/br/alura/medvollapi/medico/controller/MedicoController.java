package br.alura.medvollapi.medico.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.alura.medvollapi.medico.dto.DadosCadastroMedico;
import br.alura.medvollapi.medico.entity.Medico;
import br.alura.medvollapi.medico.repository.MedicoRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        this.medicoRepository.save(new Medico(dados));
    }
}
