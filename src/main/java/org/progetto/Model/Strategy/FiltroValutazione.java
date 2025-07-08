package org.progetto.Model.Strategy;

import org.progetto.Model.Libro;

public class FiltroValutazione implements Filtro {

    private Libro.Valutazione valutazione;

    public FiltroValutazione(Libro.Valutazione valutazione) {
        this.valutazione = valutazione;
    }

    @Override
    public boolean filtra(Libro libro) {
        return valutazione.equals(libro.getValutazione());
    }
}
