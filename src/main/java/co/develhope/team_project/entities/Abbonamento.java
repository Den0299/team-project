package co.develhope.team_project.entities;

import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Abbonamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abbonamentoId;

    @Enumerated(EnumType.STRING)
    private PianoAbbonamentoEnum pianoAbbonamento;

    private String descrizione;

    private BigDecimal prezzoMensile;
    private BigDecimal prezzoAnnuale;

    private Integer durataMesi;

    private BigDecimal sconto;

    public Abbonamento() {
    }

    public Abbonamento(Long abbonamentoId, PianoAbbonamentoEnum pianoAbbonamento, String descrizione, BigDecimal prezzoMensile, BigDecimal prezzoAnnuale, Integer durataMesi, BigDecimal sconto) {
        this.abbonamentoId = abbonamentoId;
        this.pianoAbbonamento = pianoAbbonamento;
        this.descrizione = descrizione;
        this.prezzoMensile = prezzoMensile;
        this.prezzoAnnuale = prezzoAnnuale;
        this.durataMesi = durataMesi;
        this.sconto = sconto;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzoMensile() {
        return prezzoMensile;
    }

    public void setPrezzoMensile(BigDecimal prezzoMensile) {
        this.prezzoMensile = prezzoMensile;
    }

    public BigDecimal getPrezzoAnnuale() {
        return prezzoAnnuale;
    }

    public void setPrezzoAnnuale(BigDecimal prezzoAnnuale) {
        this.prezzoAnnuale = prezzoAnnuale;
    }

    public Integer getDurataMesi() {
        return durataMesi;
    }

    public void setDurataMesi(Integer durataMesi) {
        this.durataMesi = durataMesi;
    }

    public BigDecimal getSconto() {
        return sconto;
    }

    public void setSconto(BigDecimal sconto) {
        this.sconto = sconto;
    }
}

