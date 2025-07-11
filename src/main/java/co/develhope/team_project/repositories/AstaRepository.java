package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.Asta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaRepository extends JpaRepository<Asta, Long> {
}
