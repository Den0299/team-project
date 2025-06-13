package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    private LocalDate dataAggiunta;

    @ManyToMany
    @JoinTable(
        name = "fumetto_in_wishlist",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "fumetto_id")
    )
    private List<Fumetto> fumetti;

    public Wishlist() {}

    public Wishlist(Long wishlistId, LocalDate dataAggiunta, List<Fumetto> fumetti) {
        this.wishlistId = wishlistId;
        this.dataAggiunta = dataAggiunta;
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

    public List<Fumetto> getFumetti() {
        return fumetti;
    }

    public void setFumetti(List<Fumetto> fumetti) {
        this.fumetti = fumetti;
    }
}
