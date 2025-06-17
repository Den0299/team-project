package co.develhope.team_project.entities;

import co.develhope.team_project.entities.enums.StatoAstaEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Asta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long astaId;

    private LocalDate dataInizio;
    private LocalDate dataFine;

    private BigDecimal offertaCorrente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_migliore_offerta_id")
    @JsonIgnore
    private Utente utenteMiglioreOfferta;

    @Enumerated(EnumType.STRING)
    private StatoAstaEnum statoAsta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copia_fumetto_id")
    @JsonIgnore
    private CopiaFumetto copiaFumetto;

    @OneToMany(mappedBy = "asta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IscrizioneAsta> iscrizioniAsta = new ArrayList<>();


    public Asta() {
    }

    public Asta(Long astaId, LocalDate dataInizio, LocalDate dataFine, BigDecimal offertaCorrente, StatoAstaEnum statoAsta, CopiaFumetto copiaFumetto) {
        this.astaId = astaId;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.offertaCorrente = offertaCorrente;
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

    public StatoAstaEnum getStatoAsta() {
        return statoAsta;
    }

    public void setStatoAsta(StatoAstaEnum statoAsta) {
        this.statoAsta = statoAsta;
    }

    public CopiaFumetto getCopiaFumetto() {
        return copiaFumetto;
    }

    public void setCopiaFumetto(CopiaFumetto copiaFumetto) {
        this.copiaFumetto = copiaFumetto;
    }

    public List<IscrizioneAsta> getIscrizioniAsta() {
        return iscrizioniAsta;
    }

    public void setIscrizioniAsta(List<IscrizioneAsta> iscrizioniAsta) {
        this.iscrizioniAsta = iscrizioniAsta;
    }
}

