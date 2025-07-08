package org.progetto.View.LibroView;

import org.progetto.Model.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibroListView implements LibroView {

    private final JPanel panel;
    private final JList<String> libroList;
    private final DefaultListModel<String> model;

    public LibroListView() {
        panel = new JPanel(new BorderLayout());
        model = new DefaultListModel<>();
        libroList = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(libroList);
        panel.add(scrollPane, BorderLayout.CENTER);
    }


    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void aggiorna(List<Libro> nuoviLibri) {
        model.clear();//ripulisco la lista
        for (Libro libro : nuoviLibri) { //aggiorno i dati
            model.addElement(
                    String.format("Titolo: %s -- Autore: %s -- ISBN: %s -- Genere: %s -- Valutazione: %s -- Stato: %s",
                        libro.getTitolo(),
                        libro.getAutore(),
                        libro.getISBN(),
                        libro.getGenere(),
                        (libro.getValutazione() != null ? libro.getValutazione().getDescrizione() : "Non ancora valutato."),
                        libro.getStatoLettura().name())
            );
        }
    }
}
