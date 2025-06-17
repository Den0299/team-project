package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.IscrizioneAsta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IscrizioneAstaRepository extends JpaRepository<IscrizioneAsta, Long> {
}
