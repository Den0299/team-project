package co.develhope.team_project.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DettagliOrdineInputDTO {

    @NotNull(message = "L'ID della copia del fumetto non può essere nullo")
    private Long copiaFumettoId;

    @NotNull(message = "La quantità di fumetti non può essere nulla")
    @Min(value = 1, message = "La quantità di fumetti deve essere almeno 1")
    private Integer quantitaFumetti;

    // --- Costruttori: ---
    public DettagliOrdineInputDTO() {}

    public DettagliOrdineInputDTO(Long copiaFumettoId, Integer quantitaFumetti) {
        this.copiaFumettoId = copiaFumettoId;
        this.quantitaFumetti = quantitaFumetti;
    }

    // --- Getters e Setters: ---
    public Long getCopiaFumettoId() {
        return copiaFumettoId;
    }

    public void setCopiaFumettoId(Long copiaFumettoId) {
        this.copiaFumettoId = copiaFumettoId;
    }

    public Integer getQuantitaFumetti() {
        return quantitaFumetti;
    }

    public void setQuantitaFumetti(Integer quantitaFumetti) {
        this.quantitaFumetti = quantitaFumetti;
    }
}