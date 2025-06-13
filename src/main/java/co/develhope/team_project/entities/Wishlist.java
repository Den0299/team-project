package co.develhope.team_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    private LocalDate dataAggiunta;

    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToMany(mappedBy = "wishlists")
    @JsonIgnore
    private List<Fumetto> fumetti;

    public Wishlist() {}

    public Wishlist(Long wishlistId, LocalDate dataAggiunta, Utente utente, List<Fumetto> fumetti) {
        this.wishlistId = wishlistId;
        this.dataAggiunta = dataAggiunta;
        this.utente = utente;
        this.fumetti = fumetti;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public LocalDate getDataAggiunta() {
        return dataAggiunta;
    }

    public void setDataAggiunta(LocalDate dataAggiunta) {
        this.dataAggiunta = dataAggiunta;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<Fumetto> getFumetti() {
        return fumetti;
    }

    public void setFumetti(List<Fumetto> fumetti) {
        this.fumetti = fumetti;
    }
}
