package co.develhope.team_project.services;

import co.develhope.team_project.entities.Asta;
import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.enums.StatoAstaEnum;
import co.develhope.team_project.repositories.AstaRepository;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.IscrizioneAstaRepository;
import co.develhope.team_project.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        // Imposta il riferimento alla copia fumetto
        asta.setCopiaFumetto(copiaFumetto);

        // Determina lo stato iniziale in base alla data corrente
        LocalDate oggi = LocalDate.now();

        if (oggi.isBefore(asta.getDataInizio())) {
            asta.setStatoAsta(StatoAstaEnum.NON_INIZIATA);
        } else if (!oggi.isAfter(asta.getDataFine())) {
            asta.setStatoAsta(StatoAstaEnum.IN_CORSO);
        } else {
            asta.setStatoAsta(StatoAstaEnum.CONCLUSA);
        }

        // Offerta corrente iniziale impostata a zero se null
        if (asta.getOffertaCorrente() == null) {
            asta.setOffertaCorrente(BigDecimal.ZERO);
        }

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
}
