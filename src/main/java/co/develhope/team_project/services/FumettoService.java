package co.develhope.team_project.services;

import co.develhope.team_project.entities.Fumetto;
import co.develhope.team_project.repositories.FumettoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FumettoService {

    @Autowired
    private FumettoRepository fumettoRepository;

    // crea un nuovo fumetto:
    public Fumetto addFumetto(Fumetto fumetto) {
        Fumetto nuovoFumetto = fumettoRepository.save(fumetto);

        return nuovoFumetto;
    }

    // ottieni una lista di tutti i fumetti:
    public List<Fumetto> getFumetti() {
        List<Fumetto> listaFumetti = fumettoRepository.findAll();

        return listaFumetti;
    }

    // trova un fumetto per id:
    public Optional<Fumetto> findFumetto(Long id) {
        Optional<Fumetto> fumettoOptional = fumettoRepository.findById(id);

        if (fumettoOptional.isPresent()) {
            return fumettoOptional;
        }
        return Optional.empty();
    }

    // cancella un fumetto:
    public Optional<Fumetto> deleteFumetto(Long id) {
        Optional<Fumetto> fumettoOptional = fumettoRepository.findById(id);

        if (fumettoOptional.isPresent()) {
            fumettoRepository.deleteById(id);
            return fumettoOptional;
        }
        return Optional.empty();
    }

    // modifica un fumetto:
    public Optional<Fumetto> updateFumetto(Long id, Fumetto fumettoDetails) {
        Optional<Fumetto> fumettoOptional = fumettoRepository.findById(id);

        if (fumettoOptional.isPresent()) {

            fumettoOptional.get().setTitolo(fumettoDetails.getTitolo());
            fumettoOptional.get().setAutore(fumettoDetails.getAutore());
            fumettoOptional.get().setDescrizione(fumettoDetails.getDescrizione());
            fumettoOptional.get().setEditore(fumettoDetails.getEditore());
            fumettoOptional.get().setDataPubblicazione(fumettoDetails.getDataPubblicazione());
            fumettoOptional.get().setDisponibilePerAsta(fumettoDetails.isDisponibilePerAsta());
            fumettoOptional.get().setCategoriaFumetto(fumettoDetails.getCategoriaFumetto());


            Fumetto utenteModificato = fumettoRepository.save(fumettoOptional.get());
            return Optional.of(utenteModificato);
        }
        return Optional.empty();
    }
}
