package org.progetto.View.LibroView;

import org.progetto.Model.Libro;
import org.progetto.Model.Observer.Observer;

import javax.swing.*;
import java.util.List;

public interface LibroView extends Observer {

    JComponent getComponent(); //componente che rappresenta la view
    void aggiorna(List<Libro> nuoviLibri);

}
