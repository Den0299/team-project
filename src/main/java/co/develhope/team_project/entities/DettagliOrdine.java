package co.develhope.team_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class DettagliOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dettagliOrdineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copia_fumetto_id")
    @JsonIgnore
    private CopiaFumetto copiaFumetto;

    private Integer quantitaFumetti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id")
    @JsonIgnore
    private Ordine ordine;

    public DettagliOrdine() {
    }

    public DettagliOrdine(Long dettagliOrdineId, CopiaFumetto copiaFumetto, Integer quantitaFumetti, Ordine ordine) {
        this.dettagliOrdineId = dettagliOrdineId;
        this.copiaFumetto = copiaFumetto;
        this.quantitaFumetti = quantitaFumetti;
        this.ordine = ordine;
    }

    public Long getDettagliOrdineId() {
        return dettagliOrdineId;
    }

    public void setDettagliOrdineId(Long dettagliOrdineId) {
        this.dettagliOrdineId = dettagliOrdineId;
    }

    public CopiaFumetto getCopiaFumetto() {
        return copiaFumetto;
    }

    public void setCopiaFumetto(CopiaFumetto copiaFumetto) {
        this.copiaFumetto = copiaFumetto;
    }

    public Integer getQuantitaFumetti() {
        return quantitaFumetti;
    }

    public void setQuantitaFumetti(Integer quantitaFumetti) {
        this.quantitaFumetti = quantitaFumetti;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }
}

