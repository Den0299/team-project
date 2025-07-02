package co.develhope.team_project.services;

import co.develhope.team_project.entities.Asta;
import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.repositories.AstaRepository;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.IscrizioneAstaRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AstaService {

    @Autowired
    private AstaRepository astaRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CopiaFumettoRepository copiaFumettoRepository;

    @Autowired
    private IscrizioneAstaRepository iscrizioneAstaRepository;

    public Asta createAsta(Asta asta) {
        if (asta.getCopiaFumetto() == null || asta.getCopiaFumetto().getCopiaFumettoId() == null) {
            throw new IllegalArgumentException("ID della copia fumetto mancante");
        }

        Long copiaFumettoId = asta.getCopiaFumetto().getCopiaFumettoId();

        CopiaFumetto copiaFumetto = copiaFumettoRepository.findById(copiaFumettoId)
                .orElseThrow(() -> new RuntimeException("Copia fumetto non trovata con ID: " + copiaFumettoId));

        asta.setCopiaFumetto(copiaFumetto);

        return astaRepository.save(asta);
    }

    public Optional<Asta> getAstaById(Long astaId) {
        Optional<Asta> optionalAsta = astaRepository.findById(astaId);

        return optionalAsta;
    }

    public List<Asta> getAllAste() {
        List<Asta> aste = astaRepository.findAll();

        return aste;
    }

    public Optional<Asta> updateAsta(Long id, Asta updatedAsta) {
        Optional<Asta> optionalAsta = astaRepository.findById(id);

        if (optionalAsta.isPresent()) {

            optionalAsta.get().setDataInizio(updatedAsta.getDataInizio());
            optionalAsta.get().setDataFine(updatedAsta.getDataFine());
            optionalAsta.get().setOffertaCorrente(updatedAsta.getOffertaCorrente());
            optionalAsta.get().setStatoAsta(updatedAsta.getStatoAsta());

            Asta savedAsta = astaRepository.save(optionalAsta.get());
            return Optional.of(savedAsta);
        }
        return Optional.empty();
    }

    public Optional<Asta> deleteAstaById(Long astaId) {
        Optional<Asta> optionalAsta = astaRepository.findById(astaId);

        if (optionalAsta.isPresent()) {
            astaRepository.deleteById(astaId);
            return optionalAsta;
        }
        return Optional.empty();
    }

    @Transactional
    public void faiOfferta(Long utenteId, Long astaId, BigDecimal importo) {
        Asta asta = astaRepository.findById(astaId)
                .orElseThrow(() -> new EntityNotFoundException("Asta non trovata con ID: " + astaId));

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + utenteId));

        boolean iscritto = iscrizioneAstaRepository.existsByUtenteUtenteIdAndAstaAstaId(utenteId, astaId);
        if (!iscritto) {
            throw new IllegalStateException("L'utente non è iscritto all'asta.");
        }

        BigDecimal offertaCorrente = asta.getOffertaCorrente();
        if (offertaCorrente == null) {
            offertaCorrente = BigDecimal.ZERO;
        }

        System.out.println("Offerta corrente: " + offertaCorrente);
        System.out.println("Offerta proposta: " + importo);

        if (importo.compareTo(offertaCorrente) <= 0) {
            throw new IllegalArgumentException("L'offerta deve essere maggiore dell'offerta corrente.");
        }

        // Aggiorna l'offerta corrente e l'utente migliore offerente
        asta.setOffertaCorrente(importo);
        asta.setUtenteMiglioreOfferta(utente);

        astaRepository.save(asta);
    }

}
