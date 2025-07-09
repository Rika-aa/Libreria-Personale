package org.progetto.Model.Singleton;

import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Observer.Observer;

import java.util.List;

public enum LibreriaSingleton { //patter singleton, serve per ottenere una sola istanza di libreria

    INSTANCE;

    private final GestoreLibreria gestore = new GestoreLibreria();

    public GestoreLibreria getGestore() {
        return gestore;
    }

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

    public List<Libro> getLibri(){
        return gestore.getLibri();
    }

    public void setLibri(List<Libro> libri) {
        gestore.setLibri(libri);
    }

    public void attach(Observer o){
        gestore.attach(o);
    }

    public void detach(Observer o){
        gestore.detach(o);
    }

    public void notifyObserver(){
        gestore.notifyObservers();
    }
}

