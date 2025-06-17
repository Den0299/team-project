package co.develhope.team_project.services;

import co.develhope.team_project.entities.Asta;
import co.develhope.team_project.repositories.AstaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AstaService {

    @Autowired
    private AstaRepository astaRepository;

    public Asta createAsta(Asta asta) {
        Asta nuovaAsta = astaRepository.save(asta);

        return nuovaAsta;
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
            optionalAsta.get().setUtenteMiglioreOfferta(updatedAsta.getUtenteMiglioreOfferta());
            optionalAsta.get().setStatoAsta(updatedAsta.getStatoAsta());
            optionalAsta.get().setCopiaFumetto(updatedAsta.getCopiaFumetto());

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
