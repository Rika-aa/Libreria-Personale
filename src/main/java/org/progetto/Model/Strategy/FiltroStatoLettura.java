package org.progetto.Model.Strategy;

import org.progetto.Model.Libro;

public class FiltroStatoLettura implements Filtro{

    private Libro.StatoLettura statoLettura;

    public FiltroStatoLettura(Libro.StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }

    @Override
    public boolean filtra(Libro libro) {
        return statoLettura.equals(libro.getStatoLettura());
    }
}