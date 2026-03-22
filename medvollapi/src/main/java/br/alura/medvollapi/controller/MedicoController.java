package br.alura.medvollapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.medvollapi.domain.medico.dto.DadosAtualizacaoMedico;
import br.alura.medvollapi.domain.medico.dto.DadosCadastroMedico;
import br.alura.medvollapi.domain.medico.dto.DadosDetalhamentoMedico;
import br.alura.medvollapi.domain.medico.dto.DadosListagemMedico;
import br.alura.medvollapi.domain.medico.entity.Medico;
import br.alura.medvollapi.domain.medico.repository.MedicoRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> cadastrar(@RequestBody @Valid DadosCadastroMedico dados,
            UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        this.medicoRepository.save(medico);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(
            @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
        var pagina = this.medicoRepository.findAllByAtivo(paginacao, true).map(DadosListagemMedico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN") // Permite acesso apenas quem tiver a role ADMIN // TODO: Configurar e testar o acesso do endpoint apenas para alguns usuários
    public ResponseEntity<DadosDetalhamentoMedico> recuperar(@PathVariable Long id) {
        var medico = this.medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Update automático pela JPA
    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = this.medicoRepository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var medico = this.medicoRepository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build();
    }
}
