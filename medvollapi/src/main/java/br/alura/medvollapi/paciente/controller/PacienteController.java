package br.alura.medvollapi.paciente.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.alura.medvollapi.paciente.dto.DadosCadastroPaciente;
import br.alura.medvollapi.paciente.entity.Paciente;
import br.alura.medvollapi.paciente.repository.PacienteRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
        this.pacienteRepository.save(new Paciente(dados));
    }
}
