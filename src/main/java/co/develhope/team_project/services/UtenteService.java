package co.develhope.team_project.services;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.repositories.AbbonamentoRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        List<Utente> listaUtenti = utenteRepository.findAll();

        return listaUtenti;
    }

    // trova un utente per id:
    public Optional<Utente> findUtente(Long id) {
        Optional<Utente> utenteOptional = utenteRepository.findById(id);

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
    public Utente associaAbbonamento(Long utenteId, Long abbonamentoId) {
        // 1. Trova l'utente
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));

        // 2. Trova l'istanza di Abbonamento (es. Abbonamento con PianoAbbonamentoEnum.MENSILE)
        Abbonamento abbonamento = abbonamentoRepository.findById(abbonamentoId)
                .orElseThrow(() -> new RuntimeException("Abbonamento non trovato con ID: " + abbonamentoId));

        // 3. Associa l'abbonamento all'utente
        utente.setAbbonamento(abbonamento);

        // 4. Imposta le date di inizio e fine abbonamento
        LocalDate now = LocalDate.now();
        utente.setDataInizioAbbonamento(now);

        // Accedi alla durata in giorni tramite l'enum PianoAbbonamentoEnum
        Integer durataGiorni = abbonamento.getPianoAbbonamento().getDurataGiorni();
        utente.setDataFineAbbonamento(now.plusDays(durataGiorni));

        // 5. Salva l'utente aggiornato
        return utenteRepository.save(utente);
    }
}
