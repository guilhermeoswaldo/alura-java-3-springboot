package br.alura.medvollapi.domain.consulta.entity;


import java.time.LocalDateTime;

import br.alura.medvollapi.domain.consulta.MotivoCancelamento;
import br.alura.medvollapi.domain.consulta.StatusConsulta;
import br.alura.medvollapi.domain.medico.entity.Medico;
import br.alura.medvollapi.domain.paciente.entity.Paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "consultas")
@Entity(name = "Consulta")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
    private LocalDateTime data;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;
    @Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;

    public Consulta(Medico medico, Paciente paciente, LocalDateTime data) {
        this.status = StatusConsulta.AGENDADO;
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.status = StatusConsulta.CANCELADO;
        this.motivoCancelamento = motivo;
    }
}
