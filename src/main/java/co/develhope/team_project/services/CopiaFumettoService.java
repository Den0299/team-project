package co.develhope.team_project.services;

import co.develhope.team_project.entities.CopiaFumetto;
import co.develhope.team_project.entities.Fumetto;
import co.develhope.team_project.entities.enums.StatoCopiaFumettoEnum;
import co.develhope.team_project.repositories.CopiaFumettoRepository;
import co.develhope.team_project.repositories.FumettoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CopiaFumettoService {

    @Autowired
    private CopiaFumettoRepository copiaFumettoRepository;

    @Autowired
    private FumettoRepository fumettoRepository;

    public CopiaFumetto createCopiaFumetto(CopiaFumetto copiaFumetto) {
        // Prendi il fumetto dal DB (completo)
        Fumetto fumetto = fumettoRepository.findById(copiaFumetto.getFumetto().getFumettoId())
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato"));

        copiaFumetto.setFumetto(fumetto);

        return copiaFumettoRepository.save(copiaFumetto);
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
    }

    //Segna una copia di fumetto come non disponibile, indicandone la vendita.
    @Transactional
    public void segnaComeVenduta(Long copiaId) {
        CopiaFumetto copia = copiaFumettoRepository.findById(copiaId)
                .orElseThrow(() -> new EntityNotFoundException("Copia non trovata"));
        copia.setDisponibile(false);
        copiaFumettoRepository.save(copia);
    }

    //Restituisce l'elenco di tutte le copie disponibili per un determinato fumetto.
    public List<CopiaFumetto> listaCopieDisponibili(Long fumettoId) {
        return copiaFumettoRepository.findByFumettoFumettoIdAndDisponibileTrue(fumettoId);
    }

    //metodo per aggiornare il prezzo di una copiaFumetto
    @Transactional
    public void aggiornaPrezzoCopia(Long copiaId, BigDecimal nuovoPrezzo) {
        if (nuovoPrezzo == null || nuovoPrezzo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Il prezzo deve essere positivo");
        }

        CopiaFumetto copia = copiaFumettoRepository.findById(copiaId)
                .orElseThrow(() -> new EntityNotFoundException("Copia non trovata"));

        copia.setPrezzo(nuovoPrezzo);
        copiaFumettoRepository.save(copia);
    }

    /**
     * Crea una nuova copia di fumetto e la associa a un fumetto esistente.
     * @param fumettoId L'ID del fumetto a cui associare la copia.
     * @param copiaFumetto La copia del fumetto da salvare.
     * @return Un Optional contenente la CopiaFumetto salvata se il fumetto esiste, altrimenti un Optional vuoto.
     */
    @Transactional
    public Optional<CopiaFumetto> creaCopiaFumetto(Long fumettoId, CopiaFumetto copiaFumetto) {
        Optional<Fumetto> fumettoOpt = fumettoRepository.findById(fumettoId);

        if (fumettoOpt.isPresent()) {
            Fumetto fumetto = fumettoOpt.get();
            copiaFumetto.setFumetto(fumetto); // Associa la copia al fumetto
            fumetto.addCopiaFumetto(copiaFumetto); // Mantiene la coerenza bidirezionale

            CopiaFumetto copiaSalvata = copiaFumettoRepository.save(copiaFumetto);
            return Optional.of(copiaSalvata);
        }
        return Optional.empty(); // Fumetto non trovato
    }

    public List<CopiaFumetto> findByPrezzoRange(BigDecimal prezzoMin, BigDecimal prezzoMax) {
        List<CopiaFumetto> copieFumetto = copiaFumettoRepository.findByPrezzoBetween(prezzoMin, prezzoMax);

        return copieFumetto;
    }

    public List<CopiaFumetto> findByStatoCopia(StatoCopiaFumettoEnum statoCopia) {
        List<CopiaFumetto> copieFumetto = copiaFumettoRepository.findByStatoCopiaFumetto(statoCopia);

        return copieFumetto;
    }
}
