package org.progetto.Controller.Command.Libreria;

import org.progetto.Controller.Command.Command;
import org.progetto.Controller.Memento.GestoreStatiLibreria;
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
    public boolean esegui() {
        //salvo lo stato corrente
        gestoreStati.saveState(gestoreStati.getStatoCorrente().save());

        //eseguo l'eliminazione
        LibreriaSingleton.INSTANCE.eliminaLibro(libroDaEliminare);
        return true;
    }

    @Override
    public boolean undo() {
        return gestoreStati.undo();
    }

    @Override
    public boolean redo() {
        return gestoreStati.redo();
    }
}
