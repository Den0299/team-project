package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.CopiaFumetto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CopiaFumettoRepository extends JpaRepository<CopiaFumetto, Long> {

    List<CopiaFumetto> findByFumettoFumettoIdAndDisponibileTrue(Long fumettoId);

    // Ricerca per range di prezzo
    List<CopiaFumetto> findByPrezzoBetween(BigDecimal prezzoMin, BigDecimal prezzoMax);

}
