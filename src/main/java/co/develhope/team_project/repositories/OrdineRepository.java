package co.develhope.team_project.repositories;

import co.develhope.team_project.entities.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    // Questo metodo troverà tutti gli ordini associati a un utente specifico.
    // Usiamo JOIN FETCH per caricare eager anche i DettagliOrdine, se necessario,
    // per evitare LazyInitializationException quando si serializzano gli ordini.
    @Query("SELECT o FROM Ordine o LEFT JOIN FETCH o.dettagliOrdini do WHERE o.utente.utenteId = :utenteId")
    List<Ordine> findByUtenteIdWithDettagliOrdine(Long utenteId);

    // Se volessi solo gli ordini senza i loro dettagli (se i dettagli fossero un problema di serializzazione),
    // potresti usare:
    // List<Ordine> findByUtenteUtenteId(Long utenteId);
}
