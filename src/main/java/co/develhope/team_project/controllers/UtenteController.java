package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.Utente;
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

    // ottieni una lista di utenti con un abbonamento attivo:
    @GetMapping("/abbonati-attivi")
    public ResponseEntity<List<Utente>> getUtentiConAbbonamentoAttivo() {
        try {
            List<Utente> utenti = utenteService.getUtentiConAbbonamentoAttivo();
            return ResponseEntity.ok(utenti);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero degli utenti con abbonamento attivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
