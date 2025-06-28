package co.develhope.team_project.dtos;

import co.develhope.team_project.entities.enums.StatoOrdineEnum;
import jakarta.validation.constraints.NotNull;

public class OrdineInputDTO {

    @NotNull(message = "Lo stato dell'ordine non può essere nullo")
    private StatoOrdineEnum statoOrdine;

    // --- Costruttori (opzionali ma utili) ---
    public OrdineInputDTO() {}

    public OrdineInputDTO(StatoOrdineEnum statoOrdine) {
        this.statoOrdine = statoOrdine;
    }

    // --- Getters e Setters ---
    public StatoOrdineEnum getStatoOrdine() {
        return statoOrdine;
    }

    public void setStatoOrdine(StatoOrdineEnum statoOrdine) {
        this.statoOrdine = statoOrdine;
    }
}