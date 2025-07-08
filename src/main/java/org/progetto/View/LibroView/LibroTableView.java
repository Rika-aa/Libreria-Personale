package org.progetto.View.LibroView;

import org.progetto.Model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibroTableView implements LibroView {

    private final JPanel panel;
    private final JTable table;
    private final DefaultTableModel model;

    public LibroTableView() {
        panel = new JPanel(new BorderLayout());
        model = new DefaultTableModel(
                new Object[]{"Titolo","Autore","ISBN","Genere","Valutazione","Stato"},0
        );
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public void aggiorna(List<Libro> nuoviLibri) {
        model.setRowCount(0); //ripulisco la tabella
        for(Libro libro : nuoviLibri) { //aggiorno i dati
            model.addRow(new Object[]{
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getISBN(),
                    libro.getGenere(),
                    (libro.getValutazione() != null ? libro.getValutazione().getDescrizione() : "Non ancora valutato."),
                    libro.getStatoLettura().name()
            });
        }
    }
}
