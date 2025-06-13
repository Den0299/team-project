package co.develhope.team_project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long utenteId;

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String indirizzo;

    private LocalDate dataInizioAbbonamento;

    private LocalDate dataFineAbbonamento;

    private LocalDate dataRegistrazione;

    @ManyToOne
    @JoinColumn(name = "abbonamento_id")
    private Abbonamento abbonamento;

    @OneToOne
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    @OneToMany(mappedBy = "utente")
    private List<IscrizioneAsta> iscrizioniAsta;

    public Utente() {}

    public Utente(Long utenteId, String nome, String cognome, String email, String password, String indirizzo, LocalDate dataInizioAbbonamento, LocalDate dataFineAbbonamento, LocalDate dataRegistrazione, Abbonamento abbonamento, Wishlist wishlist, List<IscrizioneAsta> iscrizioniAsta) {
        this.utenteId = utenteId;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.indirizzo = indirizzo;
        this.dataInizioAbbonamento = dataInizioAbbonamento;
        this.dataFineAbbonamento = dataFineAbbonamento;
        this.dataRegistrazione = dataRegistrazione;
        this.abbonamento = abbonamento;
        this.wishlist = wishlist;
        this.iscrizioniAsta = iscrizioniAsta;
    }

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public LocalDate getDataInizioAbbonamento() {
        return dataInizioAbbonamento;
    }

    public void setDataInizioAbbonamento(LocalDate dataInizioAbbonamento) {
        this.dataInizioAbbonamento = dataInizioAbbonamento;
    }

    public LocalDate getDataFineAbbonamento() {
        return dataFineAbbonamento;
    }

    public void setDataFineAbbonamento(LocalDate dataFineAbbonamento) {
        this.dataFineAbbonamento = dataFineAbbonamento;
    }

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Abbonamento getAbbonamento() {
        return abbonamento;
    }

    public void setAbbonamento(Abbonamento abbonamento) {
        this.abbonamento = abbonamento;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public List<IscrizioneAsta> getIscrizioniAsta() {
        return iscrizioniAsta;
    }

    public void setIscrizioniAsta(List<IscrizioneAsta> iscrizionaAsta) {
        this.iscrizioniAsta = iscrizioniAsta;
    }
}
