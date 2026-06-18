package br.alura.medvollapi.domain.medico.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.alura.medvollapi.domain.consulta.MotivoCancelamento;
import br.alura.medvollapi.domain.consulta.entity.Consulta;
import br.alura.medvollapi.domain.endereco.dto.DadosEndereco;
import br.alura.medvollapi.domain.medico.EspecialidadeMedico;
import br.alura.medvollapi.domain.medico.dto.DadosCadastroMedico;
import br.alura.medvollapi.domain.medico.entity.Medico;
import br.alura.medvollapi.domain.paciente.dto.DadosCadastroPaciente;
import br.alura.medvollapi.domain.paciente.entity.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Usa o banco de dados MySQL real porém utilizando outro database configurado no application-test.properties
@ActiveProfiles("test")
// Usa H2 em memoria com compatibilidade MySQL para executar as migrations.
//@ActiveProfiles("h2")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver null quando único médico cadastrado não está disponível na data")
    void buscarMedicoAleatorioDisponivelNaDataCenario1() {
        // given
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        var medico = this.cadastrarMedico("Medico", "medico@voll.med", "123456", EspecialidadeMedico.CARDIOLOGIA);
        var paciente = this.cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
        this.cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        // when
        var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico.CARDIOLOGIA, proximaSegundaAs10);

        // then
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver médico quando ele estiver disponível na data")
    void buscarMedicoAleatorioDisponivelNaDataCenario2() {
        // given
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        var medico = this.cadastrarMedico("Medico", "medico@voll.med", "123456", EspecialidadeMedico.CARDIOLOGIA);

        // when
        var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico.CARDIOLOGIA, proximaSegundaAs10);

        // then
        assertThat(medicoLivre).isEqualTo(medico);
    }

    @Test
    @DisplayName("Deveria devolver null quando nao existir medico disponivel da especialidade informada")
    void buscarMedicoAleatorioDisponivelNaDataCenario3() {
        // given
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        this.cadastrarMedico("Medico", "medico@voll.med", "123456", EspecialidadeMedico.CARDIOLOGIA);

        // when
        var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico.ORTOPEDIA, proximaSegundaAs10);

        // then
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver null quando o medico estiver inativo")
    void buscarMedicoAleatorioDisponivelNaDataCenario4() {
        // given
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        var medico = this.cadastrarMedico("Medico", "medico@voll.med", "123456", EspecialidadeMedico.CARDIOLOGIA);
        medico.excluir();
        this.em.flush();

        // when
        var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico.CARDIOLOGIA, proximaSegundaAs10);

        // then
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver medico quando consulta no horario estiver cancelada")
    void buscarMedicoAleatorioDisponivelNaDataCenario5() {
        // given
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        var medico = this.cadastrarMedico("Medico", "medico@voll.med", "123456", EspecialidadeMedico.CARDIOLOGIA);
        var paciente = this.cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
        var consulta = this.cadastrarConsulta(medico, paciente, proximaSegundaAs10);
        consulta.cancelar(MotivoCancelamento.PACIENTE_DESISTIU);
        this.em.flush();

        // when
        var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(EspecialidadeMedico.CARDIOLOGIA, proximaSegundaAs10);

        // then
        assertThat(medicoLivre).isEqualTo(medico);
    }

    private Consulta cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        var consulta = new Consulta(medico, paciente, data);
        this.em.persist(consulta);
        return consulta;
    }

    private Medico cadastrarMedico(String nome, String email, String crm, EspecialidadeMedico especialidade) {
        var medico = new Medico(this.dadosMedico(nome, email, crm, especialidade));
        this.em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(this.dadosPaciente(nome, email, cpf));
        this.em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, EspecialidadeMedico especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                this.dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                this.dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}
