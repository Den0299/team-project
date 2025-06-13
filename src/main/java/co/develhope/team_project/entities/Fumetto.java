package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Fumetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fumettoId;

    private String titolo;

    private String autore;

    private String editore;

    private String descrizione;

    private LocalDate dataPubblicazione;

    private boolean disponibilePerAsta;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "wishlists",
            joinColumns = @JoinColumn(name = "fumetto_id"),
            inverseJoinColumns = @JoinColumn(name = "wishlist_id"))
    private List<Wishlist> wishlists;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "copiaFumetto_id")
    private List<CopiaFumetto> copieFumetto;

    public Fumetto() {}

    public Fumetto(Long fumettoId, String titolo, String autore, String editore, String descrizione, LocalDate dataPubblicazione, boolean disponibilePerAsta, List<Wishlist> wishlists, List<CopiaFumetto> copieFumetto) {
        this.fumettoId = fumettoId;
        this.titolo = titolo;
        this.autore = autore;
        this.editore = editore;
        this.descrizione = descrizione;
        this.dataPubblicazione = dataPubblicazione;
        this.disponibilePerAsta = disponibilePerAsta;
        this.wishlists = wishlists;
        this.copieFumetto = copieFumetto;
    }

    public Long getFumettoId() {
        return fumettoId;
    }

    public void setFumettoId(Long fumettoId) {
        this.fumettoId = fumettoId;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(LocalDate dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public boolean isDisponibilePerAsta() {
        return disponibilePerAsta;
    }

    public void setDisponibilePerAsta(boolean disponibilePerAsta) {
        this.disponibilePerAsta = disponibilePerAsta;
    }

    public List<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public List<CopiaFumetto> getCopieFumetto() {
        return copieFumetto;
    }

    public void setCopieFumetto(List<CopiaFumetto> copieFumetto) {
        this.copieFumetto = copieFumetto;
    }
}
