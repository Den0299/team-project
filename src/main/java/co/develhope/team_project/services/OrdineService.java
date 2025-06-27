package co.develhope.team_project.services;

import co.develhope.team_project.dtos.DettagliOrdineInputDTO;
import co.develhope.team_project.dtos.OrdineInputDTO;
import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.enums.StatoOrdineEnum;
import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.DettagliOrdine;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.DettagliOrdineRepository;
import co.develhope.team_project.repositories.OrdineRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {
    
    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private DettagliOrdineRepository dettagliOrdineRepository;

    @Autowired
    private CopiaFumettoRepository copiaFumettoRepository;

    public Ordine createOrdine(Ordine ordine) {
        Utente utente = utenteRepository.findById(ordine.getUtente().getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        ordine.setUtente(utente);
        return ordineRepository.save(ordine);
    }

    // ottieni una lista di tutti gli ordini:
    public List<Ordine> getOrdini() {
        List<Ordine> listaOrdini = ordineRepository.findAll();

        return listaOrdini;
    }

    // trova un ordine per id:
    public Optional<Ordine> findOrdine(Long id) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {
            return ordineOptional;
        }
        return Optional.empty();
    }

    // cancella un ordine:
    public Optional<Ordine> deleteOrdine(Long id) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {
            ordineRepository.deleteById(id);
            return ordineOptional;
        }
        return Optional.empty();
    }

    // modifica un ordine:
    public Optional<Ordine> updateOrdine(Long id, Ordine ordineDetails) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {

            ordineOptional.get().setPrezzoFinale(ordineDetails.getPrezzoFinale());
            ordineOptional.get().setDataOrdine(ordineDetails.getDataOrdine());
            ordineOptional.get().setStatoOrdine(ordineDetails.getStatoOrdine());

            Ordine utenteModificato = ordineRepository.save(ordineOptional.get());
            return Optional.of(utenteModificato);
        }
        return Optional.empty();
    }

    /**
     * Crea un nuovo ordine e lo associa a un utente specifico (deprecated).
     *
     * //@param utenteId L'ID dell'utente a cui associare l'ordine.
     * //@param ordine Il nuovo oggetto Ordine da salvare.
     * //@return Un Optional contenente l'Ordine salvato se l'utente esiste, altrimenti un Optional vuoto.
     */
    /*@Transactional
    public Optional<Ordine> creaNuovoOrdine(Long utenteId, Ordine ordine) {
        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);

        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();

            // Imposta la data dell'ordine al momento corrente se non specificata dall'input
            if (ordine.getDataOrdine() == null) {
                ordine.setDataOrdine(LocalDate.now());
            }

            // Imposta lo stato dell'ordine se non specificato (o se vuoi un default)
            if (ordine.getStatoOrdine() == null) {
                ordine.setStatoOrdine(StatoOrdineEnum.IN_CONSEGNA);
            }

            // Associa l'ordine all'utente
            ordine.setUtente(utente); // Questo imposta la chiave esterna in Ordine

            // Aggiungi l'ordine alla lista degli ordini dell'utente per consistenza bidirezionale in memoria
            utente.addOrdine(ordine); // Usa il metodo helper di Utente

            // Salva l'ordine.
            // Dato che Ordine è il lato "Many" e non ha cascade per la persistenza su Utente,
            // è opportuno salvare l'ordine direttamente tramite ordineRepository.
            Ordine ordineSalvato = ordineRepository.save(ordine);

            return Optional.of(ordineSalvato);
        }
        return Optional.empty(); // Utente non trovato
    }*/

    @Transactional
    public Optional<Ordine> creaOrdinePerUtente(Long utenteId, OrdineInputDTO ordineInputDTO) { // <-- Accetta il DTO
        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);

        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();

            Ordine nuovoOrdine = new Ordine();
            nuovoOrdine.setUtente(utente);
            nuovoOrdine.setDataOrdine(LocalDate.now()); // <-- Settato nel servizio
            nuovoOrdine.setStatoOrdine(ordineInputDTO.getStatoOrdine()); // <-- Preso dal DTO
            nuovoOrdine.setPrezzoFinale(BigDecimal.ZERO); // <-- Inizializzato nel servizio

            utente.addOrdine(nuovoOrdine); // Se hai un metodo addOrdine in Utente per la relazione bidirezionale

            Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);
            return Optional.of(ordineSalvato);
        }
        return Optional.empty();
    }

    /**
     * Recupera tutti gli ordini effettuati da un utente specifico.
     *
     * @param utenteId L'ID dell'utente di cui recuperare gli ordini.
     * @return Una lista di Ordini se l'utente esiste, altrimenti una lista vuota.
     */
    @Transactional(readOnly = true) // readOnly = true ottimizza la transazione per sole letture
    public List<Ordine> getOrdiniByUtente(Long utenteId) {
        // Prima verifica se l'utente esiste.
        // Questo è importante per distinguere tra "nessun ordine per questo utente"
        // e "l'utente non esiste affatto".
        boolean utenteEsiste = utenteRepository.existsById(utenteId);

        if (utenteEsiste) {
            // Se l'utente esiste, recupera gli ordini usando la query con FETCH JOIN
            return ordineRepository.findByUtenteIdWithDettagliOrdine(utenteId);
        } else {
            return Collections.emptyList(); // Ritorna una lista vuota se l'utente non esiste
        }
    }

    /**
     * Aggiorna lo stato di un ordine specifico.
     *
     * @param ordineId L'ID dell'ordine da aggiornare.
     * @param nuovoStato Il nuovo stato da assegnare all'ordine.
     * @return Un Optional contenente l'Ordine aggiornato se trovato, altrimenti un Optional vuoto.
     */
    @Transactional
    public Optional<Ordine> aggiornaStatoOrdine(Long ordineId, StatoOrdineEnum nuovoStato) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(ordineId);

        if (ordineOpt.isPresent()) {
            Ordine ordine = ordineOpt.get();
            ordine.setStatoOrdine(nuovoStato);
            // Non è sempre necessario chiamare save() esplicitamente in un metodo @Transactional
            // se l'entità è stata recuperata all'interno della stessa transazione e modificata.
            // Hibernate rileverà il cambiamento e lo persisterà al commit.
            // Tuttavia, chiamarlo è esplicito e non fa male.
            return Optional.of(ordineRepository.save(ordine));
        }
        return Optional.empty();
    }

    /**
     * Aggiunge un nuovo dettaglio a un ordine esistente (deprecated).
     *
     * @param ordineId L'ID dell'ordine a cui aggiungere il dettaglio.
     * @param dettagliOrdineDTO Il DTO contenente i dati per il nuovo dettaglio (ID copia fumetto e quantità).
     * @return Un Optional contenente l'Ordine aggiornato se l'ordine e la copia fumetto esistono, altrimenti un Optional vuoto.
     */
    /*@Transactional
    public Optional<Ordine> aggiungiDettaglioAdOrdine(Long ordineId, DettagliOrdineInputDTO dettagliOrdineDTO) {
        // 1. Cerca l'ordine principale
        Optional<Ordine> ordineOpt = ordineRepository.findById(ordineId);
        if (ordineOpt.isEmpty()) {
            return Optional.empty(); // Ordine non trovato
        }
        Ordine ordine = ordineOpt.get();

        // 2. Cerca la copia del fumetto
        Optional<CopiaFumetto> copiaFumettoOpt = copiaFumettoRepository.findById(dettagliOrdineDTO.getCopiaFumettoId());
        if (copiaFumettoOpt.isEmpty()) {
            return Optional.empty(); // Copia Fumetto non trovata
        }
        CopiaFumetto copiaFumetto = copiaFumettoOpt.get();

        // 3. Crea l'entità DettagliOrdine
        DettagliOrdine nuoviDettagli = new DettagliOrdine();
        nuoviDettagli.setQuantitaFumetti(dettagliOrdineDTO.getQuantitaFumetti());
        nuoviDettagli.setCopiaFumetto(copiaFumetto); // Associa la copia del fumetto recuperata
        nuoviDettagli.setOrdine(ordine);            // Associa l'ordine recuperato

        // 4. Mantiene la coerenza bidirezionale
        ordine.addDettagliOrdine(nuoviDettagli); // Questo aggiunge il dettaglio alla lista nell'ordine e imposta ordine.setDettagliOrdine

        // 5. Salva il nuovo dettaglio (e Spring JPA salverà automaticamente anche l'ordine aggiornato per via della cascata)
        dettagliOrdineRepository.save(nuoviDettagli);

        // Potresti voler ricalcolare il prezzo totale dell'ordine qui
        // ordine.setPrezzoFinale(calculateNewTotal(ordine));
        // ordineRepository.save(ordine); // Salva l'ordine per aggiornare il prezzo finale, se implementato

        return Optional.of(ordine); // Restituisce l'ordine aggiornato
    }*/

    @Transactional
    public Optional<Ordine> aggiungiDettaglioAdOrdine(Long ordineId, DettagliOrdineInputDTO dettagliOrdineDTO) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(ordineId);
        if (ordineOpt.isEmpty()) {
            return Optional.empty();
        }
        Ordine ordine = ordineOpt.get();

        Optional<CopiaFumetto> copiaFumettoOpt = copiaFumettoRepository.findById(dettagliOrdineDTO.getCopiaFumettoId());
        if (copiaFumettoOpt.isEmpty()) {
            return Optional.empty();
        }
        CopiaFumetto copiaFumetto = copiaFumettoOpt.get();

        // Verifica se il dettaglio esiste già e aggiorna la quantità, o creane uno nuovo
        // Per semplicità ora, creiamo sempre un nuovo dettaglio.
        // In un caso reale, potresti voler cercare se un DettagliOrdine con la stessa copiaFumetto esiste già
        // per questo ordine e, in tal caso, aggiornare solo la quantità e il prezzo.

        DettagliOrdine nuoviDettagli = new DettagliOrdine();
        nuoviDettagli.setQuantitaFumetti(dettagliOrdineDTO.getQuantitaFumetti());
        nuoviDettagli.setCopiaFumetto(copiaFumetto);
        nuoviDettagli.setOrdine(ordine);

        ordine.addDettagliOrdine(nuoviDettagli); // Questo metodo che abbiamo appena aggiunto gestisce la bidirezionalità

        dettagliOrdineRepository.save(nuoviDettagli); // Salva il nuovo dettaglio

        // --- CALCOLA IL NUOVO PREZZO FINALE DELL'ORDINE ---
        BigDecimal costoDettaglio = copiaFumetto.getPrezzo().multiply(BigDecimal.valueOf(nuoviDettagli.getQuantitaFumetti()));
        ordine.setPrezzoFinale(ordine.getPrezzoFinale().add(costoDettaglio)); // Aggiunge il costo del nuovo dettaglio

        // Salva l'ordine aggiornato per persistere il nuovo prezzo finale
        return Optional.of(ordineRepository.save(ordine));
    }
}
