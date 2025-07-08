package org.progetto.Model.Strategy;

import org.progetto.Model.Libro;

public class FiltroAutore implements Filtro {

    private String autore;

    public FiltroAutore(String autore) {
        this.autore = autore;
    }

    @Override
    public boolean filtra(Libro libro) {
        return libro.getAutore().equalsIgnoreCase(autore);
    }
}

