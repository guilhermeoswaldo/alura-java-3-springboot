package br.alura.medvollapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.alura.medvollapi.domain.consulta.dto.DadosAgendamentoConsulta;
import br.alura.medvollapi.domain.consulta.dto.DadosDetalhamentoConsulta;
import br.alura.medvollapi.domain.consulta.service.ConsultaService;
import br.alura.medvollapi.domain.medico.EspecialidadeMedico;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockitoBean
    private ConsultaService consultaService;

    @Test
    @WithMockUser // Simula um usuario logado
    @DisplayName("Deveria devolver código HTTP 400 quando informações estão inválidas")
    void agendarCenario1() throws Exception {
        var response = mockMvc.perform(post("/consultas"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código HTTP 201 quando informações estão válidas")
    void agendarCenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1L);
        var especialidade = EspecialidadeMedico.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2L, 5L, data);
        when(consultaService.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson.write(
                                new DadosAgendamentoConsulta(2L, 5L, especialidade, data)).getJson()))
                .andReturn().getResponse();

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento).getJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver codigo HTTP 204 quando cancelamento estiver valido")
    void cancelarCenario1() throws Exception {
        var response = mockMvc.perform(post("/consultas/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idConsulta": 1,
                                  "motivo": "PACIENTE_DESISTIU"
                                }
                                """))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
