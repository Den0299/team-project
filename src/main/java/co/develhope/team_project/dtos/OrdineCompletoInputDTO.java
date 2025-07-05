package co.develhope.team_project.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrdineCompletoInputDTO {

    @NotEmpty(message = "L'ordine deve contenere almeno un dettaglio fumetto")
    @Valid // Importante: valida anche gli oggetti all'interno della lista
    private List<DettagliOrdineInputDTO> dettagli;

    // --- Costruttori: ---
    public OrdineCompletoInputDTO() {}

    public OrdineCompletoInputDTO(List<DettagliOrdineInputDTO> dettagli) {
        this.dettagli = dettagli;
    }

    // --- Getters e Setters: ---

    public List<DettagliOrdineInputDTO> getDettagli() {
        return dettagli;
    }

    public void setDettagli(List<DettagliOrdineInputDTO> dettagli) {
        this.dettagli = dettagli;
    }
}