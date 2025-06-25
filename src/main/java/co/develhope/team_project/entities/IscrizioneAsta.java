package co.develhope.team_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "iscrizioni_asta")
public class IscrizioneAsta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataIscrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asta_id", nullable = false) // Un'iscrizione deve sempre essere associata a un'asta
    @NotNull(message = "L'asta associata all'iscrizione non può essere nulla")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "iscrizioniAsta", "utenteMiglioreOfferta", "copiaFumetto"})
    private Asta asta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false) // Un'iscrizione deve sempre essere fatta da un utente
    @NotNull(message = "L'utente associato all'iscrizione non può essere nullo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "iscrizioniAsta", "abbonamento", "ordini", "asteVinte", "wishlist"})
    private Utente utente;

    public IscrizioneAsta() {
    }

    public IscrizioneAsta(LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public Asta getAsta() {
        return asta;
    }

    public void setAsta(Asta asta) {
        this.asta = asta;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IscrizioneAsta that = (IscrizioneAsta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IscrizioneAsta{" +
                "id=" + id +
                ", dataIscrizione=" + dataIscrizione +
                ", asta=" + asta +
                ", utente=" + utente +
                '}';
    }
}

