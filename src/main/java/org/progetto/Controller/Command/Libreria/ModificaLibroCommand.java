package org.progetto.Controller.Command.Libreria;

import org.progetto.Controller.Command.Command;
import org.progetto.Model.Memento.GestoreStatiLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Singleton.LibreriaSingleton;

public class ModificaLibroCommand implements Command {

    private final Libro libroModificato;
    private final GestoreStatiLibreria gestoreStati;

    public ModificaLibroCommand(Libro libroModificato, GestoreStatiLibreria gestoreStati) {
        this.libroModificato = libroModificato;
        this.gestoreStati = gestoreStati;
    }

    @Override
    public boolean esegui() {
        //eseguo la modifica
        LibreriaSingleton.INSTANCE.modificaLibro(libroModificato);
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
