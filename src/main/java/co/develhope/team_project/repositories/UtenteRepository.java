package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    // Metodo per trovare un utente tramite la sua email:
    Optional<Utente> findByEmail(String email);

    // Metodo per caricare tutti gli utenti e inizializzare la loro wishlist
    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.wishlist")
    List<Utente> findAllWithWishlist();

    // Puoi fare lo stesso per la ricerca per ID
    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.wishlist WHERE u.id = :id")
    Optional<Utente> findByIdWithWishlist(Long id);

    // Puoi anche considerare di caricare l'abbonamento qui se vuoi serializzarlo
    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.wishlist LEFT JOIN FETCH u.abbonamento WHERE u.id = :id")
    Optional<Utente> findByIdWithWishlistAndAbbonamento(@Param("id") Long id);

    // Metodo per ritornare una lista di utenti con un abbonamento attivo:
    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.wishlist LEFT JOIN FETCH u.abbonamento WHERE u.abbonamento IS NOT NULL")
    List<Utente> findUtentiAbbonatiWithWishlistAndAbbonamento();

    // --- NUOVO METODO: Recupera Utente con Wishlist e i suoi Fumetti ---
    @Query("SELECT u FROM Utente u JOIN FETCH u.wishlist w LEFT JOIN FETCH w.fumetti f WHERE u.utenteId = :utenteId")
    Optional<Utente> findByIdWithWishlistAndFumetti(Long utenteId);
}
