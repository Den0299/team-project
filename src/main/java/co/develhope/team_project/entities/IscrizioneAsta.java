package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class IscrizioneAsta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataIscrizione;

    @ManyToOne
    @JoinColumn(name = "asta_id")
    private Asta asta;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public IscrizioneAsta() {
    }

    public IscrizioneAsta(Long id, LocalDate dataIscrizione, Asta asta, Utente utente) {
        this.id = id;
        this.dataIscrizione = dataIscrizione;
        this.asta = asta;
        this.utente = utente;
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
}

