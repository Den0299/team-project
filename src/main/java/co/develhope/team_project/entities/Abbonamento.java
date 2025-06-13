package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Abbonamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abbonamentoId;

    private String nomePiano;
    private String descrizione;

    private BigDecimal prezzoMensile;
    private BigDecimal prezzoAnnuale;

    private Integer durataGiorni;
    private Integer durataMesi;

    private BigDecimal sconto;

    public Abbonamento() {
    }

    public Abbonamento(Long abbonamentoId, String nomePiano, String descrizione, BigDecimal prezzoMensile, BigDecimal prezzoAnnuale, Integer durataGiorni, Integer durataMesi, BigDecimal sconto) {
        this.abbonamentoId = abbonamentoId;
        this.nomePiano = nomePiano;
        this.descrizione = descrizione;
        this.prezzoMensile = prezzoMensile;
        this.prezzoAnnuale = prezzoAnnuale;
        this.durataGiorni = durataGiorni;
        this.durataMesi = durataMesi;
        this.sconto = sconto;
    }

    public Long getAbbonamentoId() {
        return abbonamentoId;
    }

    public void setAbbonamentoId(Long abbonamentoId) {
        this.abbonamentoId = abbonamentoId;
    }

    public String getNomePiano() {
        return nomePiano;
    }

    public void setNomePiano(String nomePiano) {
        this.nomePiano = nomePiano;
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

    public Integer getDurataGiorni() {
        return durataGiorni;
    }

    public void setDurataGiorni(Integer durataGiorni) {
        this.durataGiorni = durataGiorni;
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

