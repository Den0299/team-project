package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.DettagliOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DettagliOrdineRepository extends JpaRepository<DettagliOrdine, Long> {
}
