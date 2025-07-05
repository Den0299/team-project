package co.develhope.team_project.services;

import co.develhope.team_project.entities.Asta;
import co.develhope.team_project.entities.IscrizioneAsta;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.entities.enums.StatoAstaEnum;
import co.develhope.team_project.repositories.AstaRepository;
import co.develhope.team_project.repositories.IscrizioneAstaRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

            optionalIscrizioneAsta.get().setOfferta(updatedIscrizioneAsta.getOfferta());

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

    @Transactional
    public void faiOfferta(Long utenteId, Long astaId, BigDecimal importo) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + utenteId));

        Asta asta = astaRepository.findById(astaId)
                .orElseThrow(() -> new EntityNotFoundException("Asta non trovata con ID: " + astaId));

        // Verifica che l’asta sia attiva (facoltativo: puoi anche controllare le date)
        if (asta.getDataFine().isBefore(LocalDate.now())) {
            throw new IllegalStateException("L'asta è terminata.");
        }

        if (importo.compareTo(asta.getOffertaCorrente()) <= 0) {
            throw new IllegalArgumentException("L'offerta deve essere maggiore dell'offerta corrente.");
        }

        // Cerca se l'utente ha già fatto un'offerta
        Optional<IscrizioneAsta> iscrizioneOptional =
                iscrizioneAstaRepository.findByUtenteUtenteIdAndAstaAstaId(utenteId, astaId);

        IscrizioneAsta iscrizione;
        if (iscrizioneOptional.isPresent()) {
            iscrizione = iscrizioneOptional.get();
            iscrizione.setOfferta(importo); // aggiorna offerta
        } else {
            iscrizione = new IscrizioneAsta();
            iscrizione.setUtente(utente);
            iscrizione.setAsta(asta);
            iscrizione.setOfferta(importo); // prima offerta
        }

        // Se l'asta è NON_INIZIATA, passa a IN_CORSO
        if (asta.getStatoAsta() == StatoAstaEnum.NON_INIZIATA) {
            asta.setStatoAsta(StatoAstaEnum.IN_CORSO);
        }

        // Aggiorna l’offerta corrente e l’utente con la migliore offerta
        asta.setOffertaCorrente(importo);
        asta.setUtenteMiglioreOfferta(utente);

        iscrizioneAstaRepository.save(iscrizione);
        astaRepository.save(asta);
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
