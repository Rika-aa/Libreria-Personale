package org.progetto.Controller.Command.Libreria;

import org.progetto.Controller.Command.Command;
import org.progetto.Model.Memento.GestoreStatiLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Singleton.LibreriaSingleton;

public class AggiungiLibroCommand implements Command {

    private final Libro nuovoLibro;
    private final GestoreStatiLibreria gestoreStati;

    public AggiungiLibroCommand(Libro libro, GestoreStatiLibreria gestoreStati) {
        this.nuovoLibro = libro;
        this.gestoreStati = gestoreStati;
    }

    @Override
    public boolean esegui() {
        //eseguo l'operazione di aggiunta
        LibreriaSingleton.INSTANCE.aggiungiLibro(nuovoLibro);
        gestoreStati.saveState(LibreriaSingleton.INSTANCE.getGestore().save());
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
