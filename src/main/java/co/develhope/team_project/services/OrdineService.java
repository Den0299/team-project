package co.develhope.team_project.services;

import co.develhope.team_project.entities.Ordine;
import co.develhope.team_project.repositories.OrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {
    
    @Autowired
    private OrdineRepository ordineRepository;

    // crea un nuovo ordine:
    public Ordine addOrdine(Ordine ordine) {
        Ordine nuovoOrdine = ordineRepository.save(ordine);

        return nuovoOrdine;
    }

    // ottieni una lista di tutti gli ordini:
    public List<Ordine> getOrdini() {
        List<Ordine> listaOrdini = ordineRepository.findAll();

        return listaOrdini;
    }

    // trova un ordine per id:
    public Optional<Ordine> findOrdine(Long id) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {
            return ordineOptional;
        }
        return Optional.empty();
    }

    // cancella un ordine:
    public Optional<Ordine> deleteOrdine(Long id) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {
            ordineRepository.deleteById(id);
            return ordineOptional;
        }
        return Optional.empty();
    }

    // modifica un ordine:
    public Optional<Ordine> updateOrdine(Long id, Ordine ordineDetails) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);

        if (ordineOptional.isPresent()) {

            ordineOptional.get().setPrezzoFinale(ordineDetails.getPrezzoFinale());
            ordineOptional.get().setDataOrdine(ordineDetails.getDataOrdine());
            ordineOptional.get().setStatoOrdine(ordineDetails.getStatoOrdine());
            ordineOptional.get().setUtente(ordineDetails.getUtente());

            Ordine utenteModificato = ordineRepository.save(ordineOptional.get());
            return Optional.of(utenteModificato);
        }
        return Optional.empty();
    }
}
