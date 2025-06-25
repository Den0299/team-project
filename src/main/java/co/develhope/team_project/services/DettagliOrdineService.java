package co.develhope.team_project.services;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.DettagliOrdine;
import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.DettagliOrdineRepository;
import co.develhope.team_project.repositories.OrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DettagliOrdineService {

    @Autowired
    private DettagliOrdineRepository dettagliOrdineRepository;

    @Autowired
    private CopiaFumettoRepository copiaFumettoRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    public DettagliOrdine createDettagliOrdine(DettagliOrdine dettagliOrdine) {
        if (dettagliOrdine.getCopiaFumetto() == null || dettagliOrdine.getCopiaFumetto().getCopiaFumettoId() == null) {
            throw new IllegalArgumentException("copiaFumetto mancante o ID non presente");
        }
        if (dettagliOrdine.getOrdine() == null || dettagliOrdine.getOrdine().getOrdineId() == null) {
            throw new IllegalArgumentException("ordine mancante o ID non presente");
        }

        Long copiaFumettoId = dettagliOrdine.getCopiaFumetto().getCopiaFumettoId();
        Long ordineId = dettagliOrdine.getOrdine().getOrdineId();

        CopiaFumetto copia = copiaFumettoRepository.findById(copiaFumettoId)
                .orElseThrow(() -> new RuntimeException("Copia fumetto non trovata"));

        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        dettagliOrdine.setCopiaFumetto(copia);
        dettagliOrdine.setOrdine(ordine);

        return dettagliOrdineRepository.save(dettagliOrdine);
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

            optionalDettagliOrdine.get().setQuantitaFumetti(updatedDettagliOrdine.getQuantitaFumetti());

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
