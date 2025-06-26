package co.develhope.team_project.services;

import co.develhope.team_project.entities.Asta;
import co.develhope.team_project.entities.IscrizioneAsta;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.repositories.AstaRepository;
import co.develhope.team_project.repositories.IscrizioneAstaRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IscrizioneAstaService {

    @Autowired
    private IscrizioneAstaRepository iscrizioneAstaRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private AstaRepository astaRepository;

    public Optional<IscrizioneAsta> getIscrizioneAstaById(Long iscrizioneAstaId) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaRepository.findById(iscrizioneAstaId);

        return optionalIscrizioneAsta;
    }

    public List<IscrizioneAsta> getAllIscrizioniAsta() {
        List<IscrizioneAsta> iscrizioniAsta = iscrizioneAstaRepository.findAll();

        return iscrizioniAsta;
    }

    public Optional<IscrizioneAsta> updateIscrizioneAsta(Long id, IscrizioneAsta updatedIscrizioneAsta) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaRepository.findById(id);

        if (optionalIscrizioneAsta.isPresent()) {

            optionalIscrizioneAsta.get().setDataIscrizione(updatedIscrizioneAsta.getDataIscrizione());

            IscrizioneAsta savedIscrizioneAsta = iscrizioneAstaRepository.save(optionalIscrizioneAsta.get());
            return Optional.of(savedIscrizioneAsta);
        }
        return Optional.empty();
    }

    public Optional<IscrizioneAsta> deleteIscrizioneAstaById(Long iscrizioneAstaId) {
        Optional<IscrizioneAsta> optionalIscrizioneAsta = iscrizioneAstaRepository.findById(iscrizioneAstaId);

        if (optionalIscrizioneAsta.isPresent()) {
            iscrizioneAstaRepository.deleteById(iscrizioneAstaId);
            return optionalIscrizioneAsta;
        }
        return Optional.empty();
    }

    // Iscrive un utente a un’asta se non è già iscritto
    @Transactional
    public void createIscrizioneUtenteAsta(Long utenteId, Long astaId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + utenteId));

        Asta asta = astaRepository.findById(astaId)
                .orElseThrow(() -> new EntityNotFoundException("Asta non trovata con ID: " + astaId));

        boolean giaIscritto = iscrizioneAstaRepository.existsByUtenteUtenteIdAndAstaAstaId(utenteId, astaId);
        if (giaIscritto) {
            throw new IllegalStateException("Utente già iscritto a questa asta");
        }

        IscrizioneAsta iscrizione = new IscrizioneAsta();
        iscrizione.setUtente(utente);
        iscrizione.setAsta(asta);
        iscrizione.setDataIscrizione(LocalDate.now());

        iscrizioneAstaRepository.save(iscrizione);
    }

    // Verifica se un utente è già iscritto a una specifica asta
    public boolean isUtenteIscritto(Long utenteId, Long astaId) {
        return iscrizioneAstaRepository.existsByUtenteUtenteIdAndAstaAstaId(utenteId, astaId);
    }

    // Restituisce tutte le iscrizioni (aste) a cui un utente ha partecipato o sta partecipando
    public List<IscrizioneAsta> listaIscrizioniPerUtente(Long utenteId) {
        return iscrizioneAstaRepository.findByUtenteUtenteId(utenteId);
    }

    // Restituisce la lista degli utenti iscritti a una specifica asta
    public List<IscrizioneAsta> listaIscrizioniPerAsta(Long astaId) {
        return iscrizioneAstaRepository.findByAstaAstaId(astaId);
    }

    // Permette di cancellare l’iscrizione di un utente a una specifica asta (ad esempio se l’utente si ritira)
    @Transactional
    public void rimuoviIscrizione(Long utenteId, Long astaId) {
        Optional<IscrizioneAsta> iscrizione = iscrizioneAstaRepository.findByUtenteUtenteIdAndAstaAstaId(utenteId, astaId);
        if (iscrizione.isPresent()) {
            iscrizioneAstaRepository.delete(iscrizione.get());
        }
        }

        // Conta quanti utenti sono iscritti a una data asta
    public Integer countIscrizioniPerAsta(Long astaId) {
        return iscrizioneAstaRepository.countByAstaAstaId(astaId);
    }

}
