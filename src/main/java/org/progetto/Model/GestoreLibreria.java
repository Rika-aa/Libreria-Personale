package org.progetto.Model;

import org.progetto.Model.Memento.Memento;
import org.progetto.Model.Observer.LibreriaSubject;
import org.progetto.Model.Observer.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria implements Serializable, LibreriaSubject {

    private List<Libro> libri;
    private List<Observer> observers;

    public GestoreLibreria() {
        libri = new ArrayList<Libro>();
        observers = new ArrayList<>();
    }

    public Memento save() {
        List<Libro> copiaStato = new ArrayList<>();
        for (Libro libro : this.libri) {
            Libro copia = new Libro(libro.getTitolo(), libro.getAutore(), libro.getISBN(), libro.getGenere());
            copia.setValutazione(libro.getValutazione());
            copia.setStatoLettura(libro.getStatoLettura());
            copiaStato.add(copia);
        }
        return new Memento(copiaStato);
    }

    public void restore(Memento m) {
        this.libri.clear();
        for(Libro libro:m.getStato()){
            Libro copia = new Libro(libro.getTitolo(), libro.getAutore(), libro.getISBN(), libro.getGenere());
            copia.setValutazione(libro.getValutazione());
            copia.setStatoLettura(libro.getStatoLettura());
            this.libri.add(copia);
        }
        notifyObservers();
    }


    public void aggiungiLibro(Libro lib) {
        libri.add(lib);
        notifyObservers();
    }

    public void modificaLibro(Libro lib) {
        for(int i = 0; i < libri.size(); i++) {
            Libro l = libri.get(i);
            if(l.getISBN().equals(lib.getISBN())) {
                libri.set(i, lib);
                notifyObservers();
                return;
            }
        }
    }

    public void eliminaLibro(Libro lib) {
        libri.remove(lib);
        notifyObservers();
    }

    public List<Libro> cercaLibroTitolo(String titolo) {
        List<Libro> ret = new ArrayList();
        for(Libro l : libri) {
            if(l.getTitolo().equalsIgnoreCase(titolo)) {
                ret.add(l);
            }
        }
        return ret;
    }

    public List<Libro> cercaLibroAutore(String autore) {
        List<Libro> ret = new ArrayList();
        for(Libro l : libri) {
            if(l.getAutore().equalsIgnoreCase(autore)) {
                ret.add(l);
            }
        }
        return ret;
    }

    public Libro cercaLibro(String ISBN){
        for(Libro lib : libri){
            if(lib.getISBN().equals(ISBN)){
                return lib;
            }
        }
        return null; //libro non trovato
    }

    public List<Libro> getLibri() {
        return new ArrayList<>(libri);
    }

    public void setLibri(List<Libro> libri) {
        this.libri.clear();
        for(Libro l : libri) {
            Libro copia = new Libro(l.getTitolo(), l.getAutore(), l.getISBN(), l.getAutore());
            copia.setValutazione(l.getValutazione());
            copia.setStatoLettura(l.getStatoLettura());
            this.libri.add(copia);
        }
        notifyObservers();
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update(this);
        }
    }
}

