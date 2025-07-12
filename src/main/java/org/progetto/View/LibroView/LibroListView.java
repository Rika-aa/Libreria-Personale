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
    private final JList<Libro> libroList;
    private final DefaultListModel<Libro> model;

    public LibroListView() {
        panel = new JPanel(new BorderLayout());
        model = new DefaultListModel<>();
        libroList = new JList<>(model);

        libroList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        libroList.setSelectionBackground(new Color(173, 216, 230)); // azzurro chiaro
        libroList.setSelectionForeground(Color.BLACK);

        libroList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Libro libro) {
                    String valutazione = (libro.getValutazione() != null) ?
                            libro.getValutazione().getDescrizione() : "Non ancora valutato.";
                    label.setText(String.format(
                            "<html><b>%s</b><br>Autore: %s<br>ISBN: %s<br>Genere: %s<br>Valutazione: %s<br>Stato: %s</html>",
                            libro.getTitolo(),
                            libro.getAutore(),
                            libro.getISBN(),
                            libro.getGenere(),
                            valutazione,
                            libro.getStatoLettura().name()
                    ));
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                            BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
                return label;
            }
        });

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
            model.addElement(libro);
        }
    }

    @Override
    public void update(Object subject) {
        if(subject instanceof GestoreLibreria) {
            List<Libro> libri = ((GestoreLibreria) subject).getLibri();
            aggiorna(libri);
        }
    }

    public Libro getLibroSelezionato(){
        return libroList.getSelectedValue();
    }
}

