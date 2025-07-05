package co.develhope.team_project.services;

import co.develhope.team_project.dtos.DettagliOrdineInputDTO;
import co.develhope.team_project.dtos.OrdineInputDTO;
import co.develhope.team_project.dtos.OrdineCompletoInputDTO;
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
     * Crea un nuovo ordine per un utente, includendo tutti i dettagli dei fumetti selezionati.
     * Calcola il prezzo finale dell'ordine in base ai dettagli e imposta lo stato iniziale.
     *
     * @param utenteId L'ID dell'utente che effettua l'ordine.
     * @param ordineCompletoDTO Il DTO contenente solo la lista dei dettagli.
     * @return Un Optional contenente l'Ordine completo e salvato, o Optional.empty() se l'utente
     * o una delle copie fumetto non esiste.
     */
    @Transactional
    public Optional<Ordine> effettuaOrdine(Long utenteId, OrdineCompletoInputDTO ordineCompletoDTO) {
        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);
        if (utenteOpt.isEmpty()) {
            return Optional.empty(); // Utente non trovato
        }
        Utente utente = utenteOpt.get();

        // 1. Crea l'Ordine base
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUtente(utente);
        nuovoOrdine.setDataOrdine(LocalDate.now()); // Data impostata dal server
        nuovoOrdine.setStatoOrdine(StatoOrdineEnum.IN_ELABORAZIONE); // <-- STATO IMPOSTATO QUI DAL SERVER
        nuovoOrdine.setPrezzoFinale(BigDecimal.ZERO); // Inizializza a zero prima di calcolare

        // Questo è importante per mantenere la coerenza bidirezionale con l'utente
        utente.addOrdine(nuovoOrdine);

        // 2. Aggiungi i DettagliOrdine e calcola il prezzo finale
        BigDecimal prezzoTotaleOrdine = BigDecimal.ZERO;

        for (DettagliOrdineInputDTO dettaglioDTO : ordineCompletoDTO.getDettagli()) {
            Optional<CopiaFumetto> copiaFumettoOpt = copiaFumettoRepository.findById(dettaglioDTO.getCopiaFumettoId());
            if (copiaFumettoOpt.isEmpty()) {
                // Se una copia fumetto non esiste, si annulla l'intero ordine.
                return Optional.empty();
            }
            CopiaFumetto copiaFumetto = copiaFumettoOpt.get();

            DettagliOrdine nuovoDettaglio = new DettagliOrdine();
            nuovoDettaglio.setQuantitaFumetti(dettaglioDTO.getQuantitaFumetti());
            nuovoDettaglio.setCopiaFumetto(copiaFumetto);
            nuovoDettaglio.setOrdine(nuovoOrdine); // Collega al nuovo ordine

            // Mantiene la coerenza bidirezionale tra Ordine e DettagliOrdine
            nuovoOrdine.addDettagliOrdine(nuovoDettaglio);

            // Calcola il costo di questo dettaglio e aggiungilo al totale dell'ordine
            BigDecimal costoDettaglio = copiaFumetto.getPrezzo().multiply(BigDecimal.valueOf(nuovoDettaglio.getQuantitaFumetti()));
            prezzoTotaleOrdine = prezzoTotaleOrdine.add(costoDettaglio);
        }

        // 3. Imposta il prezzo finale calcolato sull'ordine
        nuovoOrdine.setPrezzoFinale(prezzoTotaleOrdine);

        // 4. Salva l'Ordine. Grazie a `cascade = CascadeType.ALL` su `dettagliOrdini` in Ordine,
        // tutti i DettagliOrdine associati verranno salvati automaticamente.
        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        return Optional.of(ordineSalvato);
    }
}
