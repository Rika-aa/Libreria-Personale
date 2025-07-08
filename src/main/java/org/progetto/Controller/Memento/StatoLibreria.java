package org.progetto.Controller.Memento;

import org.progetto.Model.Libro;

import java.util.ArrayList;
import java.util.List;

public class StatoLibreria { //corrisponde all'originator del pattern memento

    private List<Libro> statoCorrente;

    public StatoLibreria(List<Libro> statoCorrente) {
        this.statoCorrente = new ArrayList<Libro>(statoCorrente);
    }

    public List<Libro> getStato() {
        return statoCorrente;
    }

    public void setStato(List<Libro> statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public Memento save(){
        return new Memento(statoCorrente);
    }

    public void restore(Memento m){
        this.statoCorrente = m.getStato();
    }

}
