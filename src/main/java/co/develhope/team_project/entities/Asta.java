package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Asta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long astaId;

    private LocalDate dataInizio;
    private LocalDate dataFine;

    private BigDecimal offertaCorrente;

    private Utente utenteMiglioreOfferta;
    
    private String statoAsta;

    @ManyToOne
    @JoinColumn(name = "copia_fumetto_id")
    private CopiaFumetto copiaFumetto;

    public Asta() {
    }

    public Asta(Long astaId, LocalDate dataInizio, LocalDate dataFine, BigDecimal offertaCorrente, Utente utenteMiglioreOfferta, String statoAsta, CopiaFumetto copiaFumetto) {
        this.astaId = astaId;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.offertaCorrente = offertaCorrente;
        this.utenteMiglioreOfferta = utenteMiglioreOfferta;
        this.statoAsta = statoAsta;
        this.copiaFumetto = copiaFumetto;
    }

    public Long getAstaId() {
        return astaId;
    }

    public void setAstaId(Long astaId) {
        this.astaId = astaId;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public BigDecimal getOffertaCorrente() {
        return offertaCorrente;
    }

    public void setOffertaCorrente(BigDecimal offertaCorrente) {
        this.offertaCorrente = offertaCorrente;
    }

    public Utente getUtenteMiglioreOfferta() {
        return utenteMiglioreOfferta;
    }

    public void setUtenteMiglioreOfferta(Utente utenteMiglioreOfferta) {
        this.utenteMiglioreOfferta = utenteMiglioreOfferta;
    }

    public String getStatoAsta() {
        return statoAsta;
    }

    public void setStatoAsta(String statoAsta) {
        this.statoAsta = statoAsta;
    }

    public CopiaFumetto getCopiaFumetto() {
        return copiaFumetto;
    }

    public void setCopiaFumetto(CopiaFumetto copiaFumetto) {
        this.copiaFumetto = copiaFumetto;
    }
}

