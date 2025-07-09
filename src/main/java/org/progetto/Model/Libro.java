package org.progetto.Model;

import java.io.Serializable;

public class Libro {

    public enum StatoLettura {
        DA_LEGGERE,
        IN_LETTURA,
        LETTO
    }

    public enum Valutazione {
        SCARSO(1,"Scarso"),
        MEDIOCRE(2,"Mediocre"),
        BUONO(3,"Buono"),
        OTTIMO(4,"Ottimo"),
        ECCELLENTE(5,"Eccellente");

        private final int valore;
        private final String descrizione;

        Valutazione(int valore, String descrizione) {
            this.valore = valore;
            this.descrizione = descrizione;
        }

        public int getValore() {
            return valore;
        }

        public String getDescrizione() {
            return descrizione;
        }

    }

    private String titolo, autore;
    private String ISBN;
    private String genere;
    private Valutazione valutazione;
    private StatoLettura statoLettura;

    //Costruttore
    public Libro(String titolo, String autore, String ISBN, String genere) {
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = ISBN;
        this.genere = genere;
        this.statoLettura = StatoLettura.DA_LEGGERE; //caso di default
    }

    //Metodi accessori e mutatori
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

    public String getISBN() {
        return ISBN;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public Valutazione getValutazione() {
        return valutazione;
    }

    public void setValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }

    @Override
    public String toString() {
        return "Libro: " + titolo + " di " + autore + "\n" +
                "ISBN: " + ISBN + "\n" +
                "Genere: " + genere + "\n" +
                "Valutazione: " + (valutazione != null ? getValutazione().getDescrizione() : "Non valutato.") + "\n" +
                "Stato lettura: " + statoLettura + "\n";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return ISBN.equals(libro.ISBN);
    }

    @Override
    public int hashCode() {
        return ISBN.hashCode();
    }

}
