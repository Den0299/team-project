package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.CopiaFumetto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopiaFumettoRepository extends JpaRepository<CopiaFumetto, Long> {
}
