package co.develhope.team_project.entities;

import co.develhope.team_project.entities.enums.CategoriaFumettoEnum;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Fumetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fumettoId;

    private String titolo;

    private String autore;

    private String editore;

    private String descrizione;

    private LocalDate dataPubblicazione;

    private boolean disponibilePerAsta;

    @Enumerated(EnumType.STRING)
    private CategoriaFumettoEnum categoriaFumetto;

    public Fumetto() {}

    public Fumetto(Long fumettoId, String titolo, String autore, String editore, String descrizione, LocalDate dataPubblicazione, boolean disponibilePerAsta, CategoriaFumettoEnum categoriaFumetto) {
        this.fumettoId = fumettoId;
        this.titolo = titolo;
        this.autore = autore;
        this.editore = editore;
        this.descrizione = descrizione;
        this.dataPubblicazione = dataPubblicazione;
        this.disponibilePerAsta = disponibilePerAsta;
        this.categoriaFumetto = categoriaFumetto;
    }

    public Long getFumettoId() {
        return fumettoId;
    }

    public void setFumettoId(Long fumettoId) {
        this.fumettoId = fumettoId;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(LocalDate dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public boolean isDisponibilePerAsta() {
        return disponibilePerAsta;
    }

    public void setDisponibilePerAsta(boolean disponibilePerAsta) {
        this.disponibilePerAsta = disponibilePerAsta;
    }

    public CategoriaFumettoEnum getCategoriaFumetto() {
        return categoriaFumetto;
    }

    public void setCategoriaFumetto(CategoriaFumettoEnum categoriaFumetto) {
        this.categoriaFumetto = categoriaFumetto;
    }
}
