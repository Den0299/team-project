package co.develhope.team_project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    // --- Attributi: ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @NotNull(message = "La data di creazione della wishlist non può essere nulla")
    @PastOrPresent(message = "La data di creazione non può essere nel futuro")
    private LocalDate dataCreazione;

    // --- Chiavi esterne: ---

    @ManyToMany(fetch = FetchType.LAZY) // Lazy loading per performance
    @JoinTable(
            name = "fumetti_in_wishlist", // Nome della tabella di join
            joinColumns = @JoinColumn(name = "wishlist_id"), // Colonna che punta a Wishlist
            inverseJoinColumns = @JoinColumn(name = "fumetto_id") // Colonna che punta a Fumetto
    )
    private Set<Fumetto> fumetti = new HashSet<>();

    @OneToOne(mappedBy = "wishlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Utente utente;

    // --- Costruttori: ---

    public Wishlist() {
        this.dataCreazione = LocalDate.now();
    }

    public Wishlist(LocalDate dataCreazione) {
        this.dataCreazione = LocalDate.now();
    }

    // --- Getters e setters: ---

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Set<Fumetto> getFumetti() {
        return fumetti;
    }

    public void setFumetti(Set<Fumetto> fumetti) {
        this.fumetti = fumetti;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // --- Metodi Helper per la relazione ManyToMany con Fumetto: ---

    public void addFumetto(Fumetto fumetto) {
        if (fumetto != null && !this.fumetti.contains(fumetto)) {
            this.fumetti.add(fumetto);
            // Questa chiamata qui è importante per mantenere la coerenza in memoria
            // ma non deve causare un ciclo infinito. Il check in Fumetto.addWishlist
            // dovrebbe prevenire l'aggiunta ridondante.
            fumetto.getWishlists().add(this);
        }
    }

    public void removeFumetto(Fumetto fumetto) {
        if (fumetto != null && this.fumetti.contains(fumetto)) {
            this.fumetti.remove(fumetto);
            // Questa chiamata qui è importante per mantenere la coerenza in memoria
            fumetto.getWishlists().remove(this);
        }
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return wishlistId != null && Objects.equals(wishlistId, wishlist.wishlistId);
    }

    @Override
    public int hashCode() {
        return wishlistId != null ? Objects.hash(wishlistId) : 0;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}
