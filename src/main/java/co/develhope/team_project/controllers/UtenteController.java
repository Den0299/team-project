package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.Wishlist;
import co.develhope.team_project.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @PostMapping(path = "/create-utente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // Aggiungi consumes e produces
    public ResponseEntity<Utente> createUtente(@RequestBody Utente utente) {
        try {
            Utente newUtente = utenteService.createUtente(utente);
            return new ResponseEntity<>(newUtente, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log dell'eccezione per debugging
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ottieni una lista di tutti gli utenti:
    @GetMapping("/get-utenti")
    public ResponseEntity<List<Utente>> getUtenti() {
        List<Utente> utentiToFind = utenteService.getUtenti();

        return ResponseEntity.ok(utentiToFind);
    }

    // trova un utente per id:
    @GetMapping("/find-utente-by-id/{id}")
    public ResponseEntity<Utente> findUtente(@PathVariable Long id) {
        Optional<Utente> utenteToFind = utenteService.findUtente(id);

        if (utenteToFind.isPresent()) {
            return ResponseEntity.ok(utenteToFind.get());
        }
        return ResponseEntity.notFound().build();
    }

    // cancella un utente:
    @DeleteMapping("/delete-utente/{id}")
    public ResponseEntity<String> deleteUtente(@PathVariable Long id) {
        Optional<Utente> utenteToDelete = utenteService.deleteUtente(id);

        if (utenteToDelete.isPresent()) {
            return ResponseEntity.ok("Utente con ID '" + id + "' eliminato con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente con ID '" + id + "' non trovato.");
    }

    // modifica un utente:
    @PutMapping("/update-utente/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable Long id,
                                                         @RequestBody Utente utenteDetails) {
        Optional<Utente> utenteToUpdate = utenteService.updateUtente(id, utenteDetails);

        if (utenteToUpdate.isPresent()) {
            return ResponseEntity.ok(utenteToUpdate.get());
        }
        return ResponseEntity.notFound().build();
    }

    // associa un abbonamento ad un utente:
    @PutMapping("/assegna-abbonamento/{utenteId}")
    public ResponseEntity<Utente> assegnaAbbonamento(
            @PathVariable Long utenteId,
            @RequestBody Long abbonamentoId) {
        try {
            Optional<Utente> updatedUtente = utenteService.associaAbbonamento(utenteId, abbonamentoId);
            return ResponseEntity.ok(updatedUtente.get());
        } catch (RuntimeException e) {
            System.err.println("Errore durante l'assegnazione dell'abbonamento: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage().contains("non trovato")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // ottieni una lista di utenti con un abbonamento attivo (deprecated):
    @GetMapping("/abbonati")
    public ResponseEntity<List<Utente>> getUtentiConAbbonamentoAttivo() {
        try {
            List<Utente> utenti = utenteService.getUtentiConAbbonamentoAttivo();
            return ResponseEntity.ok(utenti);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero degli utenti con abbonamento attivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ottieni una lista di utenti con un abbonamento attivo:
    @GetMapping(path = "/abbonati-attivi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Utente>> getUtentiAbbonati() {
        List<Utente> utentiAbbonati = utenteService.getUtentiAbbonati();

        for (Utente utente : utentiAbbonati) {
            if (utente.getWishlist() != null) {
                System.out.println("Utente ID: " + utente.getUtenteId() + ", Wishlist è inizializzata: " + org.hibernate.Hibernate.isInitialized(utente.getWishlist()));
            } else {
                System.out.println("Utente ID: " + utente.getUtenteId() + ", Wishlist è null.");
            }
        }

        if (utentiAbbonati.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(utentiAbbonati, HttpStatus.OK);
    }

    /**
     * Endpoint per aggiungere un fumetto alla wishlist di un utente.
     * PUT /api/utenti/{utenteId}/add-fumetto-to-wishlist/{fumettoId}
     *
     * @param utenteId L'ID dell'utente.
     * @param fumettoId L'ID del fumetto da aggiungere.
     * @return La wishlist aggiornata o un errore se utente/fumetto non trovati.
     */
    @PutMapping(path = "/add-fumetto-to-wishlist/{utenteId}/{fumettoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wishlist> addFumettoToWishlist(
            @PathVariable Long utenteId,
            @PathVariable Long fumettoId) {

        Optional<Wishlist> updatedWishlistOpt = utenteService.addFumettoToWishlist(utenteId, fumettoId);

        if (updatedWishlistOpt.isPresent()) {
            return new ResponseEntity<>(updatedWishlistOpt.get(), HttpStatus.OK);
        } else {
            // Potresti voler distinguere tra utente non trovato e fumetto non trovato
            // Per semplicità, qui restituiamo NOT_FOUND generico
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Recupera la wishlist di un utente specifico, inclusa la lista dei fumetti.
     * GET /api/utenti/{utenteId}/wishlist
     *
     * @param utenteId L'ID dell'utente.
     * @return La wishlist dell'utente con i suoi fumetti, o 404 NOT FOUND se l'utente o la wishlist non esistono.
     */
    @GetMapping(path = "/find-wishlist-by-utenteId/{utenteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wishlist> getWishlistConFumetti(@PathVariable Long utenteId) {
        Optional<Wishlist> wishlistOpt = utenteService.getWishlistConFumetti(utenteId);

        if (wishlistOpt.isPresent()) {
            return new ResponseEntity<>(wishlistOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Rimuove un fumetto specifico dalla wishlist di un utente.
     * DELETE /api/utenti/{utenteId}/wishlist/{fumettoId}
     *
     * @param utenteId L'ID dell'utente.
     * @param fumettoId L'ID del fumetto da rimuovere.
     * @return 200 OK con la wishlist aggiornata, 404 NOT FOUND se utente/fumetto/wishlist non esistono,
     * o 204 NO CONTENT se il fumetto non era nella wishlist (potresti decidere una logica diversa qui).
     */
    @DeleteMapping(path = "/remove-fumetto-from-wishlist/{utenteId}/{fumettoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wishlist> removeFumettoFromWishlist(
            @PathVariable Long utenteId,
            @PathVariable Long fumettoId) {

        Optional<Wishlist> updatedWishlistOpt = utenteService.removeFumettoFromWishlist(utenteId, fumettoId);

        if (updatedWishlistOpt.isPresent()) {
            return new ResponseEntity<>(updatedWishlistOpt.get(), HttpStatus.OK);
        } else {
            // Potresti voler distinguere tra utente/fumetto/wishlist non trovati
            // e fumetto non presente nella wishlist. Per ora, 404 generico.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
