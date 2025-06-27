package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.services.OrdineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {
    
    @Autowired
    private OrdineService ordineService;

    // crea un nuovo ordine:
    @PostMapping("/create-ordine")
    public ResponseEntity<Ordine> createOrdine(@RequestBody Ordine ordine) {
        Ordine ordineToAdd = ordineService.createOrdine(ordine);

        return new ResponseEntity<>(ordineToAdd, HttpStatus.CREATED);
    }

    // ottieni una lista di tutti gli ordini:
    @GetMapping("/get-ordini")
    public ResponseEntity<List<Ordine>> getOrdini() {
        List<Ordine> ordiniToFind = ordineService.getOrdini();

        return ResponseEntity.ok(ordiniToFind);
    }

    // trova un ordine per id:
    @GetMapping("/find-ordine-by-id/{id}")
    public ResponseEntity<Ordine> findOrdine(@PathVariable Long id) {
        Optional<Ordine> ordineToFind = ordineService.findOrdine(id);

        if (ordineToFind.isPresent()) {
            return ResponseEntity.ok(ordineToFind.get());
        }
        return ResponseEntity.notFound().build();
    }

    // cancella un ordine:
    @DeleteMapping("/delete-ordine/{id}")
    public ResponseEntity<String> deleteOrdine(@PathVariable Long id) {
        Optional<Ordine> ordineToDelete = ordineService.deleteOrdine(id);

        if (ordineToDelete.isPresent()) {
            return ResponseEntity.ok("Ordine con ID '" + id + "' eliminato con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine con ID '" + id + "' non trovato.");
    }

    // modifica un ordine:
    @PutMapping("/update-ordine/{id}")
    public ResponseEntity<Ordine> updateOrdine(@PathVariable Long id,
                                                 @RequestBody Ordine ordineDetails) {
        Optional<Ordine> ordineToUpdate = ordineService.updateOrdine(id, ordineDetails);

        if (ordineToUpdate.isPresent()) {
            return ResponseEntity.ok(ordineToUpdate.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuovo ordine per un utente specifico.
     * POST /api/ordini/utente/{utenteId}
     *
     * @param utenteId L'ID dell'utente a cui associare l'ordine.
     * @param ordine Il nuovo oggetto Ordine da salvare (ricevuto dal corpo della richiesta).
     * @return 201 CREATED con l'Ordine creato, o 404 NOT FOUND se l'utente non esiste.
     */
    @PostMapping(path = "/create-ordine/{utenteId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ordine> creaOrdinePerUtente(
            @PathVariable Long utenteId,
            @Valid @RequestBody Ordine ordine) { // Accetta l'entità Ordine direttamente

        // La validazione dell'Ordine si baserà sulle annotazioni presenti nell'entità Ordine stessa.
        Optional<Ordine> ordineCreatoOpt = ordineService.creaNuovoOrdine(utenteId, ordine);

        if (ordineCreatoOpt.isPresent()) {
            return new ResponseEntity<>(ordineCreatoOpt.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Utente non trovato
        }
    }
}
