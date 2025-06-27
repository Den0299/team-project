package co.develhope.team_project.services;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.enums.StatoOrdineEnum;
import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.DettagliOrdine;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.DettagliOrdineRepository;
import co.develhope.team_project.repositories.OrdineRepository;
import co.develhope.team_project.repositories.UtenteRepository;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Crea un nuovo ordine e lo associa a un utente specifico.
     *
     * @param utenteId L'ID dell'utente a cui associare l'ordine.
     * @param ordine Il nuovo oggetto Ordine da salvare.
     * @return Un Optional contenente l'Ordine salvato se l'utente esiste, altrimenti un Optional vuoto.
     */
    @Transactional
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
     * Aggiunge un dettaglio a un ordine esistente.
     *
     * @param ordineId L'ID dell'ordine a cui aggiungere il dettaglio.
     * @param dettaglioOrdine Il nuovo oggetto DettagliOrdine da salvare (deve includere copiaFumetto).
     * @return Un Optional contenente l'Ordine aggiornato con il nuovo dettaglio, altrimenti un Optional vuoto.
     */
    @Transactional
    public Optional<Ordine> aggiungiDettaglioAdOrdine(Long ordineId, DettagliOrdine dettaglioOrdine) {
        Optional<Ordine> ordineOpt = ordineRepository.findById(ordineId);

        // Se CopiaFumetto non è stato impostato sul DettaglioOrdine in ingresso,
        // o se il suo ID non è valido, dobbiamo recuperarlo.
        // Assumiamo che l'ID della CopiaFumetto sia presente nell'oggetto dettaglioOrdine fornito.
        if (dettaglioOrdine.getCopiaFumetto() == null || dettaglioOrdine.getCopiaFumetto().getCopiaFumettoId() == null) {
            return Optional.empty(); // CopiaFumetto mancante o invalido nel dettaglio
        }

        Optional<CopiaFumetto> copiaFumettoOpt = copiaFumettoRepository.findById(dettaglioOrdine.getCopiaFumetto().getCopiaFumettoId());


        if (ordineOpt.isPresent() && copiaFumettoOpt.isPresent()) {
            Ordine ordine = ordineOpt.get();
            CopiaFumetto copiaFumetto = copiaFumettoOpt.get();

            dettaglioOrdine.setOrdine(ordine);          // Collega il dettaglio all'ordine
            dettaglioOrdine.setCopiaFumetto(copiaFumetto); // Associa la CopiaFumetto gestita

            // Aggiungi il dettaglio alla lista nell'ordine (per consistenza bidirezionale)
            // Se la collezione è Lazy, questo la caricherà e poi aggiungerà.
            ordine.getDettagliOrdini().add(dettaglioOrdine);

            // Salva il DettaglioOrdine. A causa di `CascadeType.ALL` su Ordine.dettagliOrdini,
            // il salvataggio dell'ordine dovrebbe persistere anche il dettaglio.
            // Tuttavia, per chiarezza e certezza, puoi salvarlo esplicitamente.
            dettagliOrdineRepository.save(dettaglioOrdine);

            // Ricarica l'ordine per assicurarsi che la collezione di dettagli sia aggiornata,
            // se si riscontrano problemi di deserializzazione successiva
            // Oppure, semplicemente salva l'ordine per propagare le modifiche se necessario
            // ordineRepository.save(ordine); // Potrebbe essere superfluo se @Transactional gestisce l'aggiornamento

            // La query `findByUtenteIdWithDettagliOrdine` nel repository già usa un `LEFT JOIN FETCH`,
            // quindi quando recuperi l'ordine in seguito, i dettagli dovrebbero essere presenti.

            return Optional.of(ordine); // Restituisci l'ordine con il nuovo dettaglio associato
        }
        return Optional.empty(); // Ordine o CopiaFumetto non trovati
    }
}
