package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordineId;

    private BigDecimal prezzoFinale;

    private LocalDate dataOrdine;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Ordine() {}

    public Ordine(Long ordineId, BigDecimal prezzoFinale, LocalDate dataOrdine, Utente utente) {
        this.ordineId = ordineId;
        this.prezzoFinale = prezzoFinale;
        this.dataOrdine = dataOrdine;
        this.utente = utente;
    }

    public Long getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(Long ordineId) {
        this.ordineId = ordineId;
    }

    public BigDecimal getPrezzoFinale() {
        return prezzoFinale;
    }

    public void setPrezzoFinale(BigDecimal prezzoFinale) {
        this.prezzoFinale = prezzoFinale;
    }

    public LocalDate getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDate dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
