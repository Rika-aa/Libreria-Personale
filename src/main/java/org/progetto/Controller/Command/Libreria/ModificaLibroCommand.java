package org.progetto.Controller.Command.Libreria;

import org.progetto.Controller.Command.Command;
import org.progetto.Controller.Memento.GestoreStatiLibreria;
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
        //salvo lo stato corrente
        gestoreStati.saveState(gestoreStati.getStatoCorrente().save());
        //eseguo la modifica
        LibreriaSingleton.INSTANCE.aggiungiLibro(libroModificato);
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
