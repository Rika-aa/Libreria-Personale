package org.progetto.View;

import org.progetto.Model.Libro;
import org.progetto.View.AbstractFactory.LibroListViewFactory;
import org.progetto.View.AbstractFactory.LibroTableViewFactory;
import org.progetto.View.LibroView.LibroView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibreriaGUI extends JFrame {

    private final JPanel panel;
    private final CardLayout cardLayout;

    private final LibroView tableView;
    private final LibroView listView;

    public LibreriaGUI() {
        setTitle("Libreria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        tableView = new LibroTableViewFactory().createView();
        listView = new LibroListViewFactory().createView();

        panel.add(tableView.getComponent(), "Tabella");
        panel.add(listView.getComponent(), "Lista");


        //JPopupMenu menu = new JPopupMenu(); //compare un pop menu cliccando con destro dell'oggetto
        JMenuBar menuBar  = createMenuBar();
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //File
        JMenu fileMenu = new JMenu("File");

        //Modifica
        JMenu editMenu = new JMenu("Modifica");
        JMenuItem aggiorna = new JMenuItem("Aggiorna");
        aggiorna.addActionListener(e -> {
            List<Libro> libriFinti = generaLibriFittizi();
            tableView.aggiorna(libriFinti);
            listView.aggiorna(libriFinti);
        });
        editMenu.add(aggiorna);

        //Visualizza
        JMenu visualizeMenu = new JMenu("Visualizza");
        JMenuItem vistaTable = new JMenuItem("Vista tabellare");
        JMenuItem vistaLista = new JMenuItem("Vista lista");
        visualizeMenu.addSeparator();

        vistaTable.addActionListener(e -> cardLayout.show(panel, "Tabella"));
        vistaLista.addActionListener(e -> cardLayout.show(panel, "Lista"));

        visualizeMenu.add(vistaTable);
        visualizeMenu.add(vistaLista);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(visualizeMenu);

        return menuBar;
    }

    //Test per vedere se funziona la modalità switch lista-tabella
    private List<Libro> generaLibriFittizi() {
        List<Libro> lista = new ArrayList<>();
        Libro l1 = new Libro("Il Nome della Rosa", "Umberto Eco", "978-88-12345678", "Giallo");
        Libro l2 = new Libro("1984", "George Orwell", "978-88-87654321", "Distopico");
        Libro l3 = new Libro("La Divina Commedia", "Dante Alighieri", "978-88-00000000", "Classico");

        lista.add(l1);
        lista.add(l2);
        lista.add(l3);
        return lista;
    }

    public static void main(String[] args) {
        LibreriaGUI libreria = new LibreriaGUI();
        libreria.setVisible(true);
    }

}
