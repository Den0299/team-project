package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.services.CopiaFumettoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/copieFumetto")
public class CopiaFumettoController {

    @Autowired
    private CopiaFumettoService copiaFumettoService;

    @PostMapping("/create")
    public ResponseEntity<CopiaFumetto> createCopiaFumetto(@RequestBody CopiaFumetto copiaFumetto) {
        CopiaFumetto createdCopiaFumetto = copiaFumettoService.createCopiaFumetto(copiaFumetto);

        return new ResponseEntity<>(createdCopiaFumetto, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CopiaFumetto>> getAllCopieFumetto() {
        List<CopiaFumetto> copieFumetto = copiaFumettoService.getAllCopieFumetto();

        return ResponseEntity.ok(copieFumetto);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CopiaFumetto> getCopiaFumettoById(@PathVariable Long id) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoService.getCopiaFumettoById(id);

        if (optionalCopiaFumetto.isPresent()) {
            return ResponseEntity.ok(optionalCopiaFumetto.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CopiaFumetto> updateCopiaFumetto(@PathVariable Long id, @RequestBody CopiaFumetto updatedCopiaFumetto) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoService.updateCopiaFumetto(id, updatedCopiaFumetto);

        if (optionalCopiaFumetto.isPresent()) {
            return ResponseEntity.ok(optionalCopiaFumetto.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCopiaFumetto(@PathVariable Long id) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoService.deleteCopiaFumettoById(id);

        if (optionalCopiaFumetto.isPresent()) {
            return ResponseEntity.ok("CopiaFumetto con ID '" + id + "' eliminata con successo.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("CopiaFumetto con ID '" + id + "' non trovata.");
    }

    @PostMapping("/copia-venduta/{copiaId}")
    public ResponseEntity<String> segnaCopiaComeVenduta(@PathVariable Long copiaId) {
        copiaFumettoService.segnaComeVenduta(copiaId);

        return ResponseEntity.ok("Copia segnata come venduta con successo.");
    }

    @GetMapping("/copie-disponibili")
    public ResponseEntity<List<CopiaFumetto>> listaCopieDisponibili(@RequestParam Long fumettoId) {
        List<CopiaFumetto> copie = copiaFumettoService.listaCopieDisponibili(fumettoId);

        return ResponseEntity.ok(copie);
    }

    @PutMapping("/aggiorna-prezzo/{copiaId}")
    public ResponseEntity<String> aggiornaPrezzoCopia(
            @PathVariable Long copiaId,
            @RequestParam BigDecimal nuovoPrezzo) {

        copiaFumettoService.aggiornaPrezzoCopia(copiaId, nuovoPrezzo);
        return ResponseEntity.ok("Prezzo aggiornato con successo.");
    }

}
