package org.progetto.View.AbstractFactory;

import org.progetto.View.LibroView.LibroListView;
import org.progetto.View.LibroView.LibroView;

public class LibroListViewFactory implements LibroViewFactory{
    @Override
    public LibroView createView() {
        return new LibroListView();
    }
}
