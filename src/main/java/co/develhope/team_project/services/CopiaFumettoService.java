package co.develhope.team_project.services;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CopiaFumettoService {

    @Autowired
    private CopiaFumettoRepository copiaFumettoRepository;

    public CopiaFumetto createCopiaFumetto(CopiaFumetto copiaFumetto) {
        CopiaFumetto nuovaCopiaFumetto = copiaFumettoRepository.save(copiaFumetto);

        return nuovaCopiaFumetto;
    }

    public Optional<CopiaFumetto> getCopiaFumettoById(Long copiaFumettoId) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoRepository.findById(copiaFumettoId);

        return optionalCopiaFumetto;
    }

    public List<CopiaFumetto> getAllCopieFumetto() {
        List<CopiaFumetto> copieFumetto = copiaFumettoRepository.findAll();

        return copieFumetto;
    }

    public Optional<CopiaFumetto> updateCopiaFumetto(Long id, CopiaFumetto updatedCopiaFumetto) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoRepository.findById(id);

        if (optionalCopiaFumetto.isPresent()) {

            optionalCopiaFumetto.get().setStatoCopiaFumetto(updatedCopiaFumetto.getStatoCopiaFumetto());
            optionalCopiaFumetto.get().setPrezzo(updatedCopiaFumetto.getPrezzo());
            optionalCopiaFumetto.get().setDisponibile(updatedCopiaFumetto.isDisponibile());
            optionalCopiaFumetto.get().setFumetto(updatedCopiaFumetto.getFumetto());

            CopiaFumetto savedCopiaFumetto = copiaFumettoRepository.save(optionalCopiaFumetto.get());
            return Optional.of(savedCopiaFumetto);
        }
        return Optional.empty();
    }

    public Optional<CopiaFumetto> deleteCopiaFumettoById(Long copiaFumettoId) {
        Optional<CopiaFumetto> optionalCopiaFumetto = copiaFumettoRepository.findById(copiaFumettoId);

        if (optionalCopiaFumetto.isPresent()) {
            copiaFumettoRepository.deleteById(copiaFumettoId);
            return optionalCopiaFumetto;
        }
        return Optional.empty();
    }}
