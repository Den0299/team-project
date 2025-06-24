package co.develhope.team_project.services;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.repositories.AbbonamentoRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            utente.setAbbonamento(abbonamento);
            utente.setDataInizioAbbonamento(LocalDate.now());
            // ... (logica per dataFineAbbonamento)

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
}
