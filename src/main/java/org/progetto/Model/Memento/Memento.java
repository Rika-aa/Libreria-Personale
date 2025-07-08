package org.progetto.Model.Memento;

import org.progetto.Model.Libro;

import java.util.ArrayList;
import java.util.List;

public class Memento {

    //lo stato corrisponde alla lista corrente di libri presenti nella libreria
    private final List<Libro> stato;

    public Memento(List<Libro> stato) {
        //Copia profonda
        this.stato = new ArrayList<Libro>();
        for(Libro libro : stato){
            Libro copia = new Libro(libro.getTitolo(), libro.getAutore(), libro.getISBN(), libro.getGenere());
            copia.setValutazione(libro.getValutazione());
            copia.setStatoLettura(libro.getStatoLettura());
            this.stato.add(copia);
        }

    }

    public List<Libro> getStato() {
        return stato;
    }

}