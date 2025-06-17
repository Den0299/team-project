package co.develhope.team_project.services;

import co.develhope.team_project.entities.DettagliOrdine;
import co.develhope.team_project.repositories.DettagliOrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DettagliOrdineService {

    @Autowired
    private DettagliOrdineRepository dettagliOrdineRepository;

    public DettagliOrdine createDettagliOrdine(DettagliOrdine dettagliOrdine) {
        DettagliOrdine nuovoDettagliOrdine = dettagliOrdineRepository.save(dettagliOrdine);

        return nuovoDettagliOrdine;
    }

    public Optional<DettagliOrdine> getDettagliOrdineById(Long dettagliOrdineId) {
        Optional<DettagliOrdine> optionalDettagliOrdine = dettagliOrdineRepository.findById(dettagliOrdineId);

        return optionalDettagliOrdine;
    }

    public List<DettagliOrdine> getAllDettagliOrdini() {
        List<DettagliOrdine> dettagliOrdini = dettagliOrdineRepository.findAll();

        return dettagliOrdini;
    }

    public Optional<DettagliOrdine> updateDettagliOrdine(Long id, DettagliOrdine updatedDettagliOrdine) {
        Optional<DettagliOrdine> optionalDettagliOrdine = dettagliOrdineRepository.findById(id);

        if (optionalDettagliOrdine.isPresent()) {

            optionalDettagliOrdine.get().setCopiaFumetto(updatedDettagliOrdine.getCopiaFumetto());
            optionalDettagliOrdine.get().setQuantitaFumetti(updatedDettagliOrdine.getQuantitaFumetti());
            optionalDettagliOrdine.get().setOrdine(updatedDettagliOrdine.getOrdine());

            DettagliOrdine savedDettagliOrdine = dettagliOrdineRepository.save(optionalDettagliOrdine.get());
            return Optional.of(savedDettagliOrdine);
        }
        return Optional.empty();
    }

    public Optional<DettagliOrdine> deleteDettagliOrdineById(Long dettagliOrdineId) {
        Optional<DettagliOrdine> optionalDettagliOrdine = dettagliOrdineRepository.findById(dettagliOrdineId);

        if (optionalDettagliOrdine.isPresent()) {
            dettagliOrdineRepository.deleteById(dettagliOrdineId);
            return optionalDettagliOrdine;
        }
        return Optional.empty();
    }
}
