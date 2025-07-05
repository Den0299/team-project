package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.Abbonamento;
import co.develhope.team_project.entities.enums.PianoAbbonamentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento, Long> {

    Optional<Abbonamento> findByPianoAbbonamento(PianoAbbonamentoEnum pianoAbbonamento);
}
