package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.IscrizioneAsta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IscrizioneAstaRepository extends JpaRepository<IscrizioneAsta, Long> {

    boolean existsByUtenteUtenteIdAndAstaAstaId(Long utenteId, Long astaId);

    List<IscrizioneAsta> findByUtenteUtenteId(Long utenteId);

    List<IscrizioneAsta> findByAstaAstaId(Long astaId);

    Optional<IscrizioneAsta> findByUtenteUtenteIdAndAstaAstaId(Long utenteId, Long astaId);

    Integer countByAstaAstaId(Long astaId);
}
