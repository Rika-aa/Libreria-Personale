package org.progetto.Controller.Command.Libreria;

import org.progetto.Controller.Command.Command;
import org.progetto.Model.Memento.GestoreStatiLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Singleton.LibreriaSingleton;

public class EliminaLibroCommand implements Command {

    private final Libro libroDaEliminare;
    private final GestoreStatiLibreria gestoreStati;

    public EliminaLibroCommand(Libro libroDaEliminare, GestoreStatiLibreria gestoreStati) {
        this.libroDaEliminare = libroDaEliminare;
        this.gestoreStati = gestoreStati;
    }

    @Override
    public void esegui() {
        //eseguo l'eliminazione
        LibreriaSingleton.INSTANCE.eliminaLibro(libroDaEliminare);
        gestoreStati.saveState(LibreriaSingleton.INSTANCE.getGestore().save());
    }

}

