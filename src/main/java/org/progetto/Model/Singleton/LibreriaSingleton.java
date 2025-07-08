package org.progetto.Model.Singleton;

import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Libro;

import java.util.List;

public enum LibreriaSingleton { //patter singleton, serve per ottenere una sola istanza di libreria

    INSTANCE;

    private final GestoreLibreria gestore = new GestoreLibreria();

    public void aggiungiLibro(Libro lib) {
        gestore.aggiungiLibro(lib);
    }

    public void modificaLibro(Libro lib) {
        gestore.modificaLibro(lib);
    }

    public void eliminaLibro(Libro lib) {
        gestore.eliminaLibro(lib);
    }

    public List<Libro> cercaLibroTitolo(String titolo) {
        return gestore.cercaLibroTitolo(titolo);
    }

    public List<Libro> cercaLibroAutore(String autore) {
        return gestore.cercaLibroAutore(autore);
    }

    public Libro cercaLibro(String ISBN){
        return gestore.cercaLibro(ISBN);
    }
}

