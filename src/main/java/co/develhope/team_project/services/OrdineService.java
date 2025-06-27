package co.develhope.team_project.services;

import co.develhope.team_project.entities.enums.StatoOrdineEnum;
import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.repositories.OrdineRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {
    
    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private UtenteRepository utenteRepository;

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
}
