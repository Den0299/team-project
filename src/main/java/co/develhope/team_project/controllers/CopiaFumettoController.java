package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.enums.StatoCopiaFumettoEnum;
import co.develhope.team_project.services.CopiaFumettoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    /**
     * Crea una nuova copia di fumetto associata a un fumetto esistente.
     * POST /api/copie-fumetto/fumetto/{fumettoId}
     * @param fumettoId L'ID del fumetto a cui associare la copia.
     * @param copiaFumetto La copia del fumetto da creare.
     * @return 201 CREATED con la copia del fumetto, o 404 NOT FOUND se il fumetto non esiste.
     */
    @PostMapping(path = "/create-copia-fumetto/{fumettoId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CopiaFumetto> createCopiaFumetto(
            @PathVariable Long fumettoId,
            @Valid @RequestBody CopiaFumetto copiaFumetto) {

        Optional<CopiaFumetto> nuovaCopiaOpt = copiaFumettoService.creaCopiaFumetto(fumettoId, copiaFumetto);

        if (nuovaCopiaOpt.isPresent()) {
            return new ResponseEntity<>(nuovaCopiaOpt.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Fumetto non trovato
        }
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

    @PutMapping("/copia-venduta/{copiaId}")
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



    @GetMapping("/filtro-prezzo")
    public ResponseEntity<List<CopiaFumetto>> getByPrezzoRange(@RequestParam BigDecimal prezzoMin,
                                                               @RequestParam BigDecimal prezzoMax) {
        List<CopiaFumetto> copieFumetto = copiaFumettoService.findByPrezzoRange(prezzoMin, prezzoMax);

        return ResponseEntity.ok(copieFumetto);
    }

    @GetMapping("/filtro-stato-copia")
    public ResponseEntity<List<CopiaFumetto>> getByStatoCopia(@RequestParam StatoCopiaFumettoEnum statoCopia) {
        List<CopiaFumetto> copieFumetto = copiaFumettoService.findByStatoCopia(statoCopia);

        return ResponseEntity.ok(copieFumetto);
    }
}
