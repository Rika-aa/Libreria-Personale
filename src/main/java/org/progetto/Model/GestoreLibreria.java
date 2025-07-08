package org.progetto.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria implements Serializable {

    private List<Libro> libri;

    public GestoreLibreria() {
        libri = new ArrayList<Libro>();
    }

    public void aggiungiLibro(Libro lib) {
        libri.add(lib);
    }

    public void modificaLibro(Libro lib) {
        for(int i = 0; i < libri.size(); i++) {
            Libro l = libri.get(i);
            if(l.getISBN().equals(lib.getISBN())) {
                libri.set(i, lib);
                return;
            }
        }
    }

    public void eliminaLibro(Libro lib) {
        libri.remove(lib);
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



}

