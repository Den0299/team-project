package co.develhope.team_project.entities;

import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Abbonamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abbonamentoId;

    @Enumerated(EnumType.STRING)
    private PianoAbbonamentoEnum pianoAbbonamento;

    @OneToMany(mappedBy = "abbonamento")
    private List<Utente> utenti;


    public Abbonamento() {
    }

    public Abbonamento(Long abbonamentoId, PianoAbbonamentoEnum pianoAbbonamento) {
        this.abbonamentoId = abbonamentoId;
        this.pianoAbbonamento = pianoAbbonamento;
    }

    public Long getAbbonamentoId() {
        return abbonamentoId;
    }

    public void setAbbonamentoId(Long abbonamentoId) {
        this.abbonamentoId = abbonamentoId;
    }

    public PianoAbbonamentoEnum getPianoAbbonamento() {
        return pianoAbbonamento;
    }

    public void setPianoAbbonamento(PianoAbbonamentoEnum pianoAbbonamento) {
        this.pianoAbbonamento = pianoAbbonamento;
    }
}

