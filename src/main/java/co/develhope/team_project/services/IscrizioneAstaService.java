package co.develhope.team_project.services;

import co.develhope.team_project.entities.IscrizioneAsta;
import co.develhope.team_project.repositories.IscrizioneAstaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IscrizioneAstaService {

    @Autowired
    private IscrizioneAstaRepository iscrizioneAstaRepository;

    public IscrizioneAsta createIscrizioneAsta(IscrizioneAsta iscrizioneAsta) {
        IscrizioneAsta nuovaIscrizioneAsta = iscrizioneAstaRepository.save(iscrizioneAsta);

        return nuovaIscrizioneAsta;
    }

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
            optionalIscrizioneAsta.get().setAsta(updatedIscrizioneAsta.getAsta());
            optionalIscrizioneAsta.get().setUtente(updatedIscrizioneAsta.getUtente());


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
}
