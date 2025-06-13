package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CopiaFumetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copiaFumettoId;

    private String statoCopiaFumetto; // Esempio: "nuovo", "usato"

    private BigDecimal prezzo;

    private boolean isDisponibile;

    @ManyToOne
    @JoinColumn(name = "dettagli_ordine_id")
    private DettagliOrdine dettagliOrdine;

    @ManyToOne
    @JoinColumn(name = "fumetto_id")
    private Fumetto fumetto;

    public CopiaFumetto() {
    }

    public CopiaFumetto(Long copiaFumettoId, String statoCopiaFumetto, BigDecimal prezzo, boolean isDisponibile, DettagliOrdine dettagliOrdine, Fumetto fumetto) {
        this.copiaFumettoId = copiaFumettoId;
        this.statoCopiaFumetto = statoCopiaFumetto;
        this.prezzo = prezzo;
        this.isDisponibile = isDisponibile;
        this.dettagliOrdine = dettagliOrdine;
        this.fumetto = fumetto;
    }

    public Long getCopiaFumettoId() {
        return copiaFumettoId;
    }

    public void setCopiaFumettoId(Long copiaFumettoId) {
        this.copiaFumettoId = copiaFumettoId;
    }

    public String getStatoCopiaFumetto() {
        return statoCopiaFumetto;
    }

    public void setStatoCopiaFumetto(String statoCopiaFumetto) {
        this.statoCopiaFumetto = statoCopiaFumetto;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isDisponibile() {
        return isDisponibile;
    }

    public void setDisponibile(boolean disponibile) {
        isDisponibile = disponibile;
    }

    public DettagliOrdine getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(DettagliOrdine dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }

    public Fumetto getFumetto() {
        return fumetto;
    }

    public void setFumetto(Fumetto fumetto) {
        this.fumetto = fumetto;
    }
}

