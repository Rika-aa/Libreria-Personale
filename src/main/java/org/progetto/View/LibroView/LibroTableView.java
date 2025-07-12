package org.progetto.View.LibroView;

import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Singleton.LibreriaSingleton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LibroTableView implements LibroView {

    private final JPanel panel;
    private final JTable table;
    private final DefaultTableModel model;

    public LibroTableView() {
        panel = new JPanel(new BorderLayout());
        model = new DefaultTableModel(
                new Object[]{"Titolo","Autore","ISBN","Genere","Valutazione","Stato"},0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //disabilito la modifica di tutte le celle: la modifica si può effettuare solo tramite popup
            }
        };
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public void impostaPopupMenu (JPopupMenu popupMenu) {
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row); // seleziona la riga su cui si clicca
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
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

    @Override
    public void update(Object subject) {
        if(subject instanceof GestoreLibreria){
            List<Libro> libri = ((GestoreLibreria) subject).getLibri();
            aggiorna(libri);
        }
    }

    public Libro getLibroSelezionato(){
        int riga = table.getSelectedRow();
        if (riga >= 0) {
            String isbn = (String) model.getValueAt(riga, 2); //prendo l'ISBM del libro selezionato
            return LibreriaSingleton.INSTANCE.cercaLibro(isbn);
        }
        return null;
    }


}
