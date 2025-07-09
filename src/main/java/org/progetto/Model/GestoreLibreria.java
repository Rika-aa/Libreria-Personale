package org.progetto.Model;

import org.progetto.Database.LibroDAO;
import org.progetto.Model.Memento.Memento;
import org.progetto.Model.Observer.LibreriaSubject;
import org.progetto.Model.Observer.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestoreLibreria implements LibreriaSubject {

    private List<Libro> libri;
    private List<Observer> observers;
    private LibroDAO libroDAO;

    public GestoreLibreria() {
        observers = new ArrayList<>();
        libroDAO = new LibroDAO();
        libri = libroDAO.getAllLibri();
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
        sincronizzaDBMemoria();
        notifyObservers();
    }

    //ricarica la tabella con quelli salvanti nella lista
    private void sincronizzaDBMemoria() {
        try{
            libroDAO.clearLibri();
            for(Libro libro:libri){
                libroDAO.addLibro(libro);
            }
            System.out.println("DB-Memoria sincronizzato");
        } catch (Exception e){
            System.err.println("Errore sinncronizzazione");
        }
    }


    public void aggiungiLibro(Libro lib) {
        if(libroDAO.addLibro(lib)) {
            libri.add(lib);
            notifyObservers();
        } else {
            System.err.println("Errore aggiunta libro nel db.");
        }
    }

    public void modificaLibro(Libro lib) {
        if(libroDAO.updateLibro(lib)) {
            for (int i = 0; i < libri.size(); i++) {
                Libro l = libri.get(i);
                if (l.getISBN().equals(lib.getISBN())) {
                    libri.set(i, lib);
                    notifyObservers();
                    return;
                }
            }
        } else {
            System.err.println("Errore modifica libro nel db.");
        }
    }

    public void eliminaLibro(Libro lib) {
        if(libroDAO.deleteLibro(lib.getISBN())) {
            libri.remove(lib);
            notifyObservers();
        } else {
            System.err.println("Errore eliminazione libro dal db.");
        }
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
        /*for(Libro lib : libri){
            if(lib.getISBN().equals(ISBN)){
                return lib;
            }
        }
        return null; //libro non trovato*/
        return libroDAO.getLibroIsbn(ISBN);
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
        sincronizzaDBMemoria();
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

