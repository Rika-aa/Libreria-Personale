package org.progetto.Model.Strategy;

import org.progetto.Model.Libro;

public interface Filtro {
    boolean filtra(Libro libro);
}
