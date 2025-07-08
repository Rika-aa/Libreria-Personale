package org.progetto.Model.Strategy;

import org.progetto.Model.Libro;

public class FiltroGenere implements Filtro {

    private String genere;

    public FiltroGenere(String genere) {
        this.genere = genere;
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getGenere().equalsIgnoreCase(genere);
    }
}