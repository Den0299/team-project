package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordineId;

    private Double prezzoFinale;

    private LocalDate dataOrdine;

    @OneToOne
    @JoinColumn(name = "dettagliOrdine_id")
    private DettagliOrdine dettagliOrdine;

    public Ordine() {}

    public Ordine(Long ordineId, Double prezzoFinale, LocalDate dataOrdine, DettagliOrdine dettagliOrdine) {
        this.ordineId = ordineId;
        this.prezzoFinale = prezzoFinale;
        this.dataOrdine = dataOrdine;
        this.dettagliOrdine = dettagliOrdine;
    }

    public Long getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(Long ordineId) {
        this.ordineId = ordineId;
    }

    public Double getPrezzoFinale() {
        return prezzoFinale;
    }

    public void setPrezzoFinale(Double prezzoFinale) {
        this.prezzoFinale = prezzoFinale;
    }

    public LocalDate getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDate dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public DettagliOrdine getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(DettagliOrdine dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }
}
