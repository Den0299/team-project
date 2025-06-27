package co.develhope.team_project.services;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.Fumetto;
import co.develhope.team_project.entities.Wishlist;
import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import co.develhope.team_project.repositories.AbbonamentoRepository;
import co.develhope.team_project.repositories.FumettoRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private AbbonamentoRepository abbonamentoRepository;

    @Autowired
    private FumettoRepository fumettoRepository;

    // crea un nuovo utente:
    public Utente createUtente(Utente utente) {
        Optional<Utente> existingUser = utenteRepository.findByEmail(utente.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Questa email è già stata utilizzata da un altro utente");
        }
        utente.setDataRegistrazione(LocalDate.now());
        return utenteRepository.save(utente);
    }

    // ottieni una lista di tutti gli utenti:
    public List<Utente> getUtenti() {
        // List<Utente> listaUtenti = utenteRepository.findAll();
        List<Utente> listaUtenti = utenteRepository.findAllWithWishlist();

        return listaUtenti;
    }

    // trova un utente per id:
    public Optional<Utente> findUtente(Long id) {
        // Optional<Utente> utenteOptional = utenteRepository.findById(id);
        Optional<Utente> utenteOptional = utenteRepository.findByIdWithWishlist(id);

        if (utenteOptional.isPresent()) {
            return utenteOptional;
        }
        return Optional.empty();
    }

    // cancella un utente:
    public Optional<Utente> deleteUtente(Long id) {
        Optional<Utente> utenteOptional = utenteRepository.findById(id);

        if (utenteOptional.isPresent()) {
            utenteRepository.deleteById(id);
            return utenteOptional;
        }
        return Optional.empty();
    }

    // modifica un utente:
    public Optional<Utente> updateUtente(Long id, Utente utenteDetails) {
        Optional<Utente> utenteOptional = utenteRepository.findById(id);

        if (utenteOptional.isPresent()) {

            utenteOptional.get().setNome(utenteDetails.getNome());
            utenteOptional.get().setCognome(utenteDetails.getCognome());
            utenteOptional.get().setEmail(utenteDetails.getEmail());
            utenteOptional.get().setPassword(utenteDetails.getPassword());
            utenteOptional.get().setIndirizzo(utenteDetails.getIndirizzo());
            utenteOptional.get().setDataInizioAbbonamento(utenteDetails.getDataInizioAbbonamento());
            utenteOptional.get().setDataFineAbbonamento(utenteDetails.getDataFineAbbonamento());
            utenteOptional.get().setDataRegistrazione(utenteDetails.getDataRegistrazione());
            utenteOptional.get().setRuoloUtente(utenteDetails.getRuoloUtente());

            Utente utenteModificato = utenteRepository.save(utenteOptional.get());
            return Optional.of(utenteModificato);
        }
        return Optional.empty();
    }

    // verifica con un booleano se un utente esiste per id:
    @Transactional(readOnly = true)
    public boolean existsById(Long utenteId) {
        return utenteRepository.existsById(utenteId);
    }

    // associa un abbonamento ad un utente:
    @Transactional // Garantisce che l'operazione sia atomica
    public Optional<Utente> associaAbbonamento(Long utenteId, Long abbonamentoId) {
        // 1. Trova l'utente
        Optional<Utente> utenteOpt = utenteRepository.findByIdWithWishlistAndAbbonamento(utenteId);

        // 2. Trova l'istanza di Abbonamento (es. Abbonamento con PianoAbbonamentoEnum.MENSILE)
        Optional<Abbonamento> abbonamentoOpt = abbonamentoRepository.findById(abbonamentoId);

        if (utenteOpt.isPresent() && abbonamentoOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            Abbonamento abbonamento = abbonamentoOpt.get();

            PianoAbbonamentoEnum piano = abbonamento.getPianoAbbonamento();

            if (piano == null) {
                // Gestisci il caso in cui il piano di abbonamento non sia definito nell'Abbonamento
                return Optional.empty(); // O lancia un'eccezione specifica
            }

            utente.setAbbonamento(abbonamento);
            utente.setDataInizioAbbonamento(LocalDate.now());

            // --- QUI LA MODIFICA CHIAVE: Utilizza la durataGiorni dall'enum ---
            Integer durataGiorni = piano.getDurataGiorni();
            if (durataGiorni != null && durataGiorni > 0) {
                utente.setDataFineAbbonamento(LocalDate.now().plusDays(durataGiorni));
            } else {
                // Gestisci il caso in cui la durata non sia definita o sia zero
                utente.setDataFineAbbonamento(null);
            }
            // ---------------------------------------------------------------

            Utente updatedUtente = utenteRepository.save(utente);
            // La wishlist (e abbonamento) sono già caricate grazie al FETCH JOIN
            return Optional.of(updatedUtente);
        }
        return Optional.empty();
    }

    // ottieni una lista di utenti con un abbonamento attivo:
    @Transactional(readOnly = true)
    public List<Utente> getUtentiConAbbonamentoAttivo() {
        List<Utente> allUtenti = utenteRepository.findAll();
        LocalDate today = LocalDate.now();

        List<Utente> utentiAttivi = new ArrayList<>();

        for (Utente utente : allUtenti) {
            if (utente.getAbbonamento() != null && // L'utente deve avere un abbonamento assegnato
                    utente.getDataFineAbbonamento() != null && // La data di fine abbonamento deve essere impostata
                    !utente.getDataFineAbbonamento().isBefore(today)) { // La data di fine abbonamento non è passata
                utentiAttivi.add(utente);
            }
        }
        return utentiAttivi;
    }

    @Transactional
    public List<Utente> getUtentiAbbonati() {
        List<Utente> listaUtentiAbbonati = utenteRepository.findUtentiAbbonatiWithWishlistAndAbbonamento();

        return listaUtentiAbbonati;
    }

    @Transactional // Necessario per garantire che le relazioni Lazy siano gestite correttamente e per il salvataggio
    public Optional<Wishlist> addFumettoToWishlist(Long utenteId, Long fumettoId) {
        // 1. Trova l'utente e inizializza la sua wishlist
        // Usiamo findByIdWithWishlist per assicurarci che la wishlist sia caricata
        Optional<Utente> utenteOpt = utenteRepository.findByIdWithWishlist(utenteId);
        if (utenteOpt.isEmpty()) {
            // Utente non trovato
            return Optional.empty();
        }
        Utente utente = utenteOpt.get();

        // Assicurati che l'utente abbia una wishlist, altrimenti creane una nuova
        // Questo è importante se un utente può essere creato senza una wishlist associata
        if (utente.getWishlist() == null) {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUtente(utente); // Collega la wishlist all'utente
            utente.setWishlist(newWishlist); // Collega l'utente alla wishlist
            // La nuova wishlist verrà salvata a cascata con l'utente se cascade è impostato su ALL
        }

        Wishlist wishlist = utente.getWishlist();

        // Inizializza esplicitamente la collezione di fumetti nella wishlist (se FetchType.LAZY)
        // anche se è @JsonIgnore, potresti aver bisogno di accedervi per modificarla
        Hibernate.initialize(wishlist.getFumetti());


        // 2. Trova il fumetto
        Optional<Fumetto> fumettoOpt = fumettoRepository.findById(fumettoId);
        if (fumettoOpt.isEmpty()) {
            // Fumetto non trovato
            return Optional.empty();
        }
        Fumetto fumetto = fumettoOpt.get();

        // 3. Aggiungi il fumetto alla wishlist
        // Assicurati che il metodo addFumetto nella Wishlist gestisca anche il lato Fumetto se necessario
        wishlist.addFumetto(fumetto);

        // 4. Salva la wishlist (o l'utente, dipende dalla configurazione del cascade)
        // Poiché la wishlist è mappata by in Utente con CascadeType.ALL, salvare l'utente è sufficiente.
        // Se la Wishlist avesse un proprio repository e volessi salvare solo la wishlist, potresti farlo lì.
        // utenteRepository.save(utente); // Questo salverà anche la wishlist a cascata.

        // Ritorna la wishlist aggiornata
        return Optional.of(wishlist);
    }

    /**
     * Recupera la wishlist di un utente specifico, inclusa la lista dei fumetti.
     *
     * @param utenteId L'ID dell'utente.
     * @return Un Optional contenente la Wishlist con i fumetti se trovata, altrimenti un Optional vuoto.
     */
    @Transactional // @Transactional è importante per gestire le relazioni lazy all'interno della sessione Hibernate.
    public Optional<Wishlist> getWishlistConFumetti(Long utenteId) {
        Optional<Utente> utenteOpt = utenteRepository.findByIdWithWishlistAndFumetti(utenteId);

        if (utenteOpt.isPresent()) {
            return Optional.ofNullable(utenteOpt.get().getWishlist());
        }
        return Optional.empty();
    }

    /**
     * Rimuove un fumetto dalla wishlist di un utente specifico.
     *
     * @param utenteId L'ID dell'utente proprietario della wishlist.
     * @param fumettoId L'ID del fumetto da rimuovere.
     * @return Un Optional contenente la Wishlist aggiornata se l'operazione ha successo, altrimenti un Optional vuoto.
     */
    @Transactional // @Transactional è cruciale perché modifichiamo una relazione gestita.
    public Optional<Wishlist> removeFumettoFromWishlist(Long utenteId, Long fumettoId) {
        // Carica l'utente e la sua wishlist (e i fumetti) in eager per evitare LazyInitializationException
        Optional<Utente> utenteOpt = utenteRepository.findByIdWithWishlistAndFumetti(utenteId);
        Optional<Fumetto> fumettoOpt = fumettoRepository.findById(fumettoId);

        if (utenteOpt.isPresent() && fumettoOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            Fumetto fumetto = fumettoOpt.get();
            Wishlist wishlist = utente.getWishlist();

            if (wishlist != null) {
                // Usa il metodo helper removeFumetto per gestire la rimozione bidirezionale
                wishlist.removeFumetto(fumetto);
                // Salva l'utente per persistere i cambiamenti nella wishlist associata
                utenteRepository.save(utente);
                return Optional.of(wishlist); // Restituisce la wishlist aggiornata
            }
        }
        return Optional.empty(); // Utente, fumetto o wishlist non trovati
    }
}
