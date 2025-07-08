package org.progetto.View.AbstractFactory;

import org.progetto.View.LibroView.LibroView;
import org.progetto.View.LibroView.LibroTableView;

public class LibroTableViewFactory implements LibroViewFactory{

    @Override
    public LibroView createView() {
        return new LibroTableView();
    }
}
