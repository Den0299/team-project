package co.develhope.team_project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "dettagli_ordine")
public class DettagliOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dettagliOrdineId;

    @NotNull(message = "La quantità di fumetti non può essere nulla")
    @Min(value = 1, message = "La quantità di fumetti deve essere almeno 1")
    @Column(nullable = false) // La quantità non può essere nulla nel DB
    private Integer quantitaFumetti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copia_fumetto_id", nullable = false)
    @NotNull(message = "La copia del fumetto non può essere nulla per un dettaglio dell'ordine")
    @JsonIgnore
    private CopiaFumetto copiaFumetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false)
    @NotNull(message = "L'ordine non può essere nullo per un dettaglio dell'ordine")
    @JsonBackReference
    private Ordine ordine;

    public DettagliOrdine() {
    }

    public DettagliOrdine(Integer quantitaFumetti) {
        this.quantitaFumetti = quantitaFumetti;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DettagliOrdine that = (DettagliOrdine) o;
        return Objects.equals(dettagliOrdineId, that.dettagliOrdineId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dettagliOrdineId);
    }

    @Override
    public String toString() {
        return "DettagliOrdine{" +
                "dettagliOrdineId=" + dettagliOrdineId +
                ", copiaFumetto=" + copiaFumetto +
                ", quantitaFumetti=" + quantitaFumetti +
                ", ordine=" + ordine +
                '}';
    }
}

