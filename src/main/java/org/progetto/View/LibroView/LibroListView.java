package org.progetto.View.LibroView;

import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Singleton.LibreriaSingleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LibroListView implements LibroView {

    private final JPanel panel;
    private final JList<String> libroList;
    private JPopupMenu popupMenu;
    private final DefaultListModel<String> model;

    public LibroListView() {
        panel = new JPanel(new BorderLayout());
        model = new DefaultListModel<>();
        libroList = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(libroList);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public void impostaPopupMenu (JPopupMenu popupMenu) {
        libroList.setComponentPopupMenu(popupMenu);
        libroList.addMouseListener(new MouseAdapter() {
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
                int index = libroList.locationToIndex(e.getPoint());
                if (index != -1) {
                    libroList.setSelectedIndex(index); // seleziona l'elemento cliccato
                    popupMenu.show(libroList, e.getX(), e.getY());
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

    @Override
    public void update(Object subject) {
        if(subject instanceof LibreriaSingleton) {
            List<Libro> libri = ((LibreriaSingleton) subject).getLibri();
            aggiorna(libri);
        }
    }

    public Libro getLibroSelezionato(){
        String selected = libroList.getSelectedValue();
        if (selected != null) {
            String[] parts = selected.split("--");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("ISBN:")) {
                    String isbn = part.substring("ISBN:".length()).trim();
                    return LibreriaSingleton.INSTANCE.cercaLibro(isbn);
                }
            }
        }
        return null;
    }
}
