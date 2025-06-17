package co.develhope.team_project.services;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.repositories.AbbonamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbbonamentoService {

    @Autowired
    private AbbonamentoRepository abbonamentoRepository;

    public Abbonamento createAbbonamento(Abbonamento abbonamento) {
        Abbonamento nuovoAbbonamento = abbonamentoRepository.save(abbonamento);

        return nuovoAbbonamento;

    }

    public Optional<Abbonamento> getAbbonamentoById(Long abbonamentoId) {
        Optional<Abbonamento> optionalAbbonamento = abbonamentoRepository.findById(abbonamentoId);

        return optionalAbbonamento;
    }

    public List<Abbonamento> getAllAbbonamenti() {
        List<Abbonamento> abbonamenti = abbonamentoRepository.findAll();

        return abbonamenti;
    }

    public Optional<Abbonamento> updateAbbonamento(Long id, Abbonamento updatedAbbonamento) {
        Optional<Abbonamento> optionalAbbonamento = abbonamentoRepository.findById(id);

        if (optionalAbbonamento.isPresent()) {

            optionalAbbonamento.get().setPianoAbbonamento(updatedAbbonamento.getPianoAbbonamento());

            Abbonamento savedAbbonamento = abbonamentoRepository.save(optionalAbbonamento.get());
            return Optional.of(savedAbbonamento);
        }
        return Optional.empty();
    }

    public Optional<Abbonamento> deleteAbbonamentoById(Long abbonamentoId) {
        Optional<Abbonamento> optionalAbbonamento = abbonamentoRepository.findById(abbonamentoId);

        if (optionalAbbonamento.isPresent()) {
            abbonamentoRepository.deleteById(abbonamentoId);
            return optionalAbbonamento;
        }
        return Optional.empty();
    }
}
