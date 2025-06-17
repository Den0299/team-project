package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.Abbonamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento, Long> {
}
