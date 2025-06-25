package co.develhope.team_project.entities;

import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "abbonamenti")
public class Abbonamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abbonamentoId;

    @Enumerated(EnumType.STRING)
    private PianoAbbonamentoEnum pianoAbbonamento;

    @OneToMany(mappedBy = "abbonamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Previene loop infiniti nella serializzazione JSON
    private List<Utente> utenti = new ArrayList<>();


    public Abbonamento() {
    }

    public Abbonamento(PianoAbbonamentoEnum pianoAbbonamento) {
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

    public List<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Abbonamento that = (Abbonamento) o;
        return Objects.equals(abbonamentoId, that.abbonamentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(abbonamentoId);
    }

    @Override
    public String toString() {
        return "Abbonamento{" +
                "abbonamentoId=" + abbonamentoId +
                ", pianoAbbonamento=" + pianoAbbonamento +
                ", utenti=" + utenti +
                '}';
    }
}

