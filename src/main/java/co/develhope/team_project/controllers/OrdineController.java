package co.develhope.team_project.controllers;

import co.develhope.team_project.dtos.OrdineCompletoInputDTO;
import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.entities.enums.StatoOrdineEnum;
import co.develhope.team_project.services.OrdineService;
import co.develhope.team_project.services.UtenteService;
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

    @Autowired
    private UtenteService utenteService;

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
     * Recupera tutti gli ordini di un utente specifico.
     * GET /api/ordini/utente/{utenteId}
     *
     * @param utenteId L'ID dell'utente.
     * @return Una lista di Ordini, o 404 NOT FOUND se l'utente non esiste.
     */
    @GetMapping(path = "/find-ordini/{utenteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ordine>> getOrdiniByUtente(@PathVariable Long utenteId) {
        List<Ordine> ordini = ordineService.getOrdiniByUtente(utenteId);

        // Se la lista è vuota, potremmo voler distinguere se è perché l'utente non esiste
        // o se l'utente esiste ma non ha ordini.
        // Dato che il service restituisce una lista vuota se l'utente non esiste,
        // e una lista (anche vuota) se l'utente esiste ma non ha ordini,
        // un HttpStatus.OK con una lista vuota è tecnicamente corretto anche per utente non trovato.
        // Se volessi un 404 per utente non trovato, dovresti fare un check esplicito nel controller
        // sull'esistenza dell'utente (magari tramite un metodo in UtenteService).
        if (ordini.isEmpty() && !utenteService.existsById(utenteId)) { // Assumi che UtenteService abbia un existsById
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(ordini, HttpStatus.OK);
        }
    }

    /**
     * Aggiorna lo stato di un ordine specifico.
     * PUT /api/ordini/{ordineId}/stato
     *
     * @param ordineId L'ID dell'ordine da aggiornare.
     * @param nuovoStato Il nuovo stato (come stringa, che verrà convertita nell'enum).
     * @return 200 OK con l'Ordine aggiornato, o 404 NOT FOUND se l'ordine non esiste.
     */
    @PutMapping(path = "/update-stato-ordine/{ordineId}", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ordine> updateStatoOrdine(
            @PathVariable Long ordineId,
            @RequestBody String nuovoStato) {

        try {
            StatoOrdineEnum statoEnum = StatoOrdineEnum.valueOf(nuovoStato.toUpperCase());
            Optional<Ordine> updatedOrdineOpt = ordineService.aggiornaStatoOrdine(ordineId, statoEnum);

            if (updatedOrdineOpt.isPresent()) {
                return new ResponseEntity<>(updatedOrdineOpt.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Permette a un utente di effettuare un ordine completo con i dettagli dei fumetti.
     * POST /api/ordini/{utenteId}/effettua-ordine
     * @param utenteId L'ID dell'utente che effettua l'ordine.
     * @param ordineCompletoDTO Il DTO contenente solo la lista dei dettagli.
     * @return 201 CREATED con l'ordine completo creato, o 404 NOT FOUND se l'utente o un fumetto non esiste.
     */
    @PostMapping(path = "/effettua-ordine/{utenteId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ordine> effettuaOrdine(
            @PathVariable Long utenteId,
            @Valid @RequestBody OrdineCompletoInputDTO ordineCompletoDTO) { // Il DTO ora non contiene statoOrdine

        Optional<Ordine> ordineCreatoOpt = ordineService.effettuaOrdine(utenteId, ordineCompletoDTO);

        if (ordineCreatoOpt.isPresent()) {
            return new ResponseEntity<>(ordineCreatoOpt.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Utente o una CopiaFumetto non trovati
        }
    }
}
