package org.progetto.View.LibroView;

import org.progetto.Model.Libro;

import javax.swing.*;
import java.util.List;

public interface LibroView {

    JComponent getComponent(); //componente che rappresenta la view
    void aggiorna(List<Libro> nuoviLibri);

}
