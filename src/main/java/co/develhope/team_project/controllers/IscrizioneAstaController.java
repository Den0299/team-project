package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.IscrizioneAsta;
import co.develhope.team_project.services.IscrizioneAstaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/iscrizioniAsta")
public class IscrizioneAstaController {

    @Autowired
    private IscrizioneAstaService iscrizioneAstaService;

    @GetMapping("/get-all")
    public ResponseEntity<List<IscrizioneAsta>> getAllIscrizioniAsta() {
        List<IscrizioneAsta> iscrizioneAsta = iscrizioneAstaService.getAllIscrizioniAsta();
        return ResponseEntity.ok(iscrizioneAsta);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<IscrizioneAsta> getIscrizioneAstaById(@PathVariable Long id) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaService.getIscrizioneAstaById(id);

        if (optionalIscrizioneAsta.isPresent()) {
            return ResponseEntity.ok(optionalIscrizioneAsta.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<IscrizioneAsta> updateIscrizioneAsta(@PathVariable Long id, @RequestBody IscrizioneAsta updatedIscrizioneAsta) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaService.updateIscrizioneAsta(id, updatedIscrizioneAsta);

        if (optionalIscrizioneAsta.isPresent()) {
            return ResponseEntity.ok(optionalIscrizioneAsta.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIscrizioneAsta(@PathVariable Long id) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaService.deleteIscrizioneAstaById(id);

        if (optionalIscrizioneAsta.isPresent()) {
            return ResponseEntity.ok("IscrizioneAsta con ID '" + id + "' eliminata con successo.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("IscrizioneAsta con ID '" + id + "' non trovata.");
    }

    @PostMapping("/iscrivi-utente-asta")
    public ResponseEntity<String> createIscrizioneUtenteAsta(
            @RequestParam Long astaId,
            @RequestParam Long utenteId) {

        iscrizioneAstaService.createIscrizioneUtenteAsta(utenteId, astaId);
        return ResponseEntity.ok("Utente con ID '" + utenteId + "' iscritto con successo all'asta con id '" + astaId + "'.");
    }

    @GetMapping("/verifica-iscrizione-utente")
    public ResponseEntity<Boolean> isUtenteIscritto(
            @RequestParam Long utenteId,
            @RequestParam Long astaId) {

        boolean iscritto = iscrizioneAstaService.isUtenteIscritto(utenteId, astaId);
        return ResponseEntity.ok(iscritto);
    }

    // Lista iscrizioni per utente
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<IscrizioneAsta>> getIscrizioniPerUtente(@PathVariable Long utenteId) {
        List<IscrizioneAsta> iscrizioni = iscrizioneAstaService.listaIscrizioniPerUtente(utenteId);

        return ResponseEntity.ok(iscrizioni);
    }

    // Lista iscrizioni per asta
    @GetMapping("/asta/{astaId}")
    public ResponseEntity<List<IscrizioneAsta>> getIscrizioniPerAsta(@PathVariable Long astaId) {
        List<IscrizioneAsta> iscrizioni = iscrizioneAstaService.listaIscrizioniPerAsta(astaId);

        return ResponseEntity.ok(iscrizioni);
    }

    // Rimuovi iscrizione
    @DeleteMapping("/rimuovi-iscrizione-utente")
    public ResponseEntity<String> rimuoviIscrizione(
            @RequestParam Long utenteId,
            @RequestParam Long astaId) {

        iscrizioneAstaService.rimuoviIscrizione(utenteId, astaId);
        return ResponseEntity.ok("Iscrizione rimossa con successo.");
    }

    // Conta iscrizioni per asta
    @GetMapping("/conteggio-iscrizioni/{astaId}")
    public ResponseEntity<Integer> countIscrizioni(@PathVariable Long astaId) {

        Integer count = iscrizioneAstaService.countIscrizioniPerAsta(astaId);
        return ResponseEntity.ok(count);
    }


}
