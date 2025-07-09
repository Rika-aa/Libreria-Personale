package org.progetto.View;

import org.progetto.Controller.Command.Libreria.AggiungiLibroCommand;
import org.progetto.Controller.Command.Libreria.EliminaLibroCommand;
import org.progetto.Controller.Command.Libreria.ModificaLibroCommand;
import org.progetto.Model.Libro;
import org.progetto.Model.Memento.GestoreStatiLibreria;
import org.progetto.Model.Singleton.LibreriaSingleton;
import org.progetto.View.AbstractFactory.LibroListViewFactory;
import org.progetto.View.AbstractFactory.LibroTableViewFactory;
import org.progetto.View.LibroView.LibroListView;
import org.progetto.View.LibroView.LibroTableView;
import org.progetto.View.LibroView.LibroView;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class LibreriaGUI extends JFrame {

    //pannello e layout manager per le due view
    private final JPanel panel;
    private final CardLayout cardLayout;

    private JPopupMenu popupMenu;

    private String viewCorrente;
    private static final String TABLE = "Tabella", LIST = "Lista";

    private final LibroView tableView;
    private final LibroView listView;

    private JTextField cercaField;
    private JComboBox<String> statoFiltraComboBox;
    private JComboBox<String> valutazioneFiltraComboBox;

    private final GestoreStatiLibreria gestoreStati;

    public LibreriaGUI() {
        setTitle("Libreria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        gestoreStati = new GestoreStatiLibreria(LibreriaSingleton.INSTANCE.getGestore());

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        tableView = new LibroTableViewFactory().createView();
        listView = new LibroListViewFactory().createView();

        //aggiunta viste
        panel.add(tableView.getComponent(), TABLE);
        panel.add(listView.getComponent(), LIST);

        //collegamento vista observer
        LibreriaSingleton.INSTANCE.attach(tableView);
        LibreriaSingleton.INSTANCE.attach(listView);

        viewCorrente = TABLE;

        createPopupMenu(); //contiene operazioni di delete e modifica
        JToolBar toolBar = createToolBar(); //operazioni di filtraggio e ricerca
        aggiornaView();

        JMenuBar menuBar  = createMenuBar(); //operazioni exit dal programma, cambio vista (tabella o lista) e undo e redo
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        cardLayout.show(panel, TABLE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //File
        JMenu fileMenu = new JMenu("File");
        JMenuItem creaLibro = new JMenuItem("Crea libro");
        creaLibro.addActionListener(e -> creaNuovoLibro());
        creaLibro.setAccelerator(KeyStroke.getKeyStroke("control N"));
        fileMenu.add(creaLibro);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control W")); //chiude il programma
        fileMenu.add(exitItem);

        // Modifica
        JMenu editMenu = new JMenu("Modifica");
        JMenuItem aggiorna = new JMenuItem("Aggiorna");
        aggiorna.addActionListener(e -> aggiornaView());
        editMenu.add(aggiorna);
        editMenu.addSeparator();

        JMenuItem undoItem = new JMenuItem("Annulla");
        undoItem.addActionListener(e -> {
            if (gestoreStati.undo()) {
                aggiornaView();
            } else {
                JOptionPane.showMessageDialog(this, "Nessuna operazione da annullare", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        undoItem.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        editMenu.add(undoItem);

        JMenuItem redoItem = new JMenuItem("Ripeti");
        redoItem.addActionListener(e -> {
            if (gestoreStati.redo()) {
                aggiornaView();
            } else {
                JOptionPane.showMessageDialog(this, "Nessuna operazione da ripristinare", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        redoItem.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        editMenu.add(redoItem);

        //Visualizza
        JMenu visualizeMenu = new JMenu("Visualizza");
        JMenuItem vistaTable = new JMenuItem("Vista tabellare");
        JMenuItem vistaLista = new JMenuItem("Vista lista");

        vistaTable.addActionListener(e -> {
            cardLayout.show(panel, TABLE);
            viewCorrente = TABLE;
        });
        vistaLista.addActionListener(e -> {
            cardLayout.show(panel, LIST);
            viewCorrente = LIST;
        });

        vistaTable.setAccelerator(KeyStroke.getKeyStroke("control T"));
        vistaLista.setAccelerator(KeyStroke.getKeyStroke("control L"));

        visualizeMenu.add(vistaTable);
        visualizeMenu.addSeparator();
        visualizeMenu.add(vistaLista);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(visualizeMenu);

        return menuBar;
    }

    private void creaNuovoLibro() {
        JTextField titoloField = new JTextField();
        JTextField autoreField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField genereField = new JTextField();

        //valutazione
        Libro.Valutazione[] valutazioniEnum = Libro.Valutazione.values();
        String[] nomiValutazioni = new String[valutazioniEnum.length + 1];
        nomiValutazioni[0] = "Nessuna Valutazione"; //default
        for (int i = 0; i < valutazioniEnum.length; i++) {
            nomiValutazioni[i + 1] = valutazioniEnum[i].getDescrizione();
        }
        JComboBox<String> valutazioneComboBox = new JComboBox<>(nomiValutazioni);
        valutazioneComboBox.setSelectedIndex(0);

        //StatoLettura
        JComboBox<Libro.StatoLettura> statoLetturaComboBox = new JComboBox<>(Libro.StatoLettura.values());
        statoLetturaComboBox.setSelectedItem(Libro.StatoLettura.DA_LEGGERE); // Imposta DA_LEGGERE come default

        Object[] message = {
                "Titolo:", titoloField,
                "Autore:", autoreField,
                "ISBN:", isbnField,
                "Genere:", genereField,
                "Valutazione:", valutazioneComboBox,
                "Stato di Lettura:", statoLetturaComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Crea libro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String titolo = titoloField.getText().trim();
            String autore = autoreField.getText().trim();
            String isbn = isbnField.getText().trim();
            String genere = genereField.getText().trim();

            if (titolo.isEmpty() || autore.isEmpty() || isbn.isEmpty() || genere.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserire tutti i campi! Titolo, Autore, ISBN, Genere devono essere compilati.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (LibreriaSingleton.INSTANCE.cercaLibro(isbn) != null) {
                JOptionPane.showMessageDialog(this, "Un libro con questo ISBN esiste già. L'ISBN deve essere unico.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Libro nuovoLibro = new Libro(titolo, autore, isbn, genere);

            String selectedValutazioneDesc = (String) valutazioneComboBox.getSelectedItem();
            if (!"Nessuna Valutazione".equals(selectedValutazioneDesc)) {
                for (Libro.Valutazione val : Libro.Valutazione.values()) {
                    if (val.getDescrizione().equals(selectedValutazioneDesc)) {
                        nuovoLibro.setValutazione(val);
                        break;
                    }
                }
            } else {
                nuovoLibro.setValutazione(null); // L'utente ha scelto di non assegnare una valutazione
            }

            // Recupera e imposta lo StatoLettura
            nuovoLibro.setStatoLettura((Libro.StatoLettura) statoLetturaComboBox.getSelectedItem());

            AggiungiLibroCommand aggiungi = new AggiungiLibroCommand(nuovoLibro, gestoreStati);
            aggiungi.esegui();
            aggiornaView();
        } else {
            JOptionPane.showMessageDialog(this, "Operazione annullata.");
        }
    }

    private void aggiornaView() {
        List<Libro> libriAttuali = LibreriaSingleton.INSTANCE.getLibri();
        tableView.aggiorna(libriAttuali);
        listView.aggiorna(libriAttuali);
        applicaFiltriERicerca();
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();

        ((LibroTableView) tableView).impostaPopupMenu(popupMenu);
        ((LibroListView) listView).impostaPopupMenu(popupMenu);

        JMenuItem modificaItem = new JMenuItem("Modifica libro");
        JMenuItem eliminaItem = new JMenuItem("Elimina libro");

        modificaItem.addActionListener(e -> {
            Libro libroDaModificare = getLibroSelezionato();
            if (libroDaModificare != null) {
                modificaLibro(libroDaModificare);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        eliminaItem.addActionListener(e -> {
            Libro libroDaEliminare = getLibroSelezionato();
            if (libroDaEliminare != null) {
                eliminaLibro(libroDaEliminare);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un libro da eliminare.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        popupMenu.add(modificaItem);
        popupMenu.add(eliminaItem);
    }

    //metodo per capire che libro è stato selezionato
    private Libro getLibroSelezionato() {
        if(viewCorrente.equals(TABLE)){
            return ((LibroTableView) tableView).getLibroSelezionato();
        } else if(viewCorrente.equals(LIST)){
            return ((LibroListView) listView).getLibroSelezionato();
        }
        return null;//libro non trovato
    }

    private void eliminaLibro(Libro libro) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare il libro:\n" +
                        "Titolo: " + libro.getTitolo() + "\n" +
                        "Autore: " + libro.getAutore() + "\n" +
                        "ISBN: " + libro.getISBN() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            EliminaLibroCommand elimina = new EliminaLibroCommand(libro, gestoreStati);
            elimina.esegui();
            aggiornaView();
        } else {
            JOptionPane.showMessageDialog(this, "Operazione annullata.");
        }
    }

    private void modificaLibro(Libro libro) {
        JTextField titoloField = new JTextField(libro.getTitolo());
        JTextField autoreField = new JTextField(libro.getAutore());
        JTextField isbnField = new JTextField(libro.getISBN());
        isbnField.setEditable(false);//ISBN originale
        JTextField genereField = new JTextField(libro.getGenere());

        // Gestione Valutazione
        JComboBox<Libro.Valutazione> valutazioneComboBox = new JComboBox<>(Libro.Valutazione.values());
        if (libro.getValutazione() != null) {
            valutazioneComboBox.setSelectedItem(libro.getValutazione());
        }

        // Gestione StatoLettura
        JComboBox<Libro.StatoLettura> statoLetturaComboBox = new JComboBox<>(Libro.StatoLettura.values());
        statoLetturaComboBox.setSelectedItem(libro.getStatoLettura());

        Object[] message = {
                "Titolo:", titoloField,
                "Autore:", autoreField,
                "ISBN:", isbnField,
                "Genere:", genereField,
                "Valutazione:", valutazioneComboBox,
                "Stato:", statoLetturaComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Modifica libro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Libro libroModificato = new Libro(
                    titoloField.getText().trim(),
                    autoreField.getText().trim(),
                    isbnField.getText().trim(),
                    genereField.getText().trim()
            );
            libroModificato.setValutazione((Libro.Valutazione) valutazioneComboBox.getSelectedItem());
            libroModificato.setStatoLettura((Libro.StatoLettura) statoLetturaComboBox.getSelectedItem());

            ModificaLibroCommand modifica = new ModificaLibroCommand(libroModificato, gestoreStati);
            modifica.esegui();
            aggiornaView();
        } else {
            JOptionPane.showMessageDialog(this, "Modifica annullata.");
        }
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar("Filtra e Cerca");
        toolBar.setFloatable(false);

        // Ricerca
        toolBar.add(new JLabel("Cerca: "));
        cercaField = new JTextField(20);
        cercaField.addActionListener(e ->applicaFiltriERicerca()); //se premo invio parte la ricerca
        toolBar.add(cercaField);
        toolBar.addSeparator();

        // Filtro StatoLettura
        toolBar.add(new JLabel("Stato: "));
        String[] stati = new String[Libro.StatoLettura.values().length + 1];
        stati[0] = "Tutti";
        for (int i = 0; i < Libro.StatoLettura.values().length; i++) {
            stati[i + 1] = Libro.StatoLettura.values()[i].name();
        }
        statoFiltraComboBox = new JComboBox<>(stati);
        statoFiltraComboBox.setSelectedIndex(0); // default: tutti
        toolBar.add(statoFiltraComboBox);
        toolBar.addSeparator();

        // Filtro Valutazione
        toolBar.add(new JLabel("Valutazione: "));
        String[] valutazioni = new String[Libro.Valutazione.values().length + 1];
        valutazioni[0] = "Tutte";
        for (int i = 0; i < Libro.Valutazione.values().length; i++) {
            valutazioni[i + 1] = Libro.Valutazione.values()[i].getDescrizione();
        }
        valutazioneFiltraComboBox = new JComboBox<>(valutazioni);
        valutazioneFiltraComboBox.setSelectedIndex(0); // default: tutte
        toolBar.add(valutazioneFiltraComboBox);
        toolBar.addSeparator();

        // Pulsante applica
        JButton applyFilterButton = new JButton("Applica Filtri");
        applyFilterButton.addActionListener(e -> applicaFiltriERicerca());
        toolBar.add(applyFilterButton);

        return toolBar;
    }

    //;etodo per applicare i filtri e la ricerca
    private void applicaFiltriERicerca() {
        List<Libro> libriFiltrati = LibreriaSingleton.INSTANCE.getLibri();

        //filtro per StatoLettura
        String selectedStato = (String) statoFiltraComboBox.getSelectedItem();
        if (selectedStato != null && !selectedStato.equals("Tutti")) {
            Libro.StatoLettura statoDaFiltrare = Libro.StatoLettura.valueOf(selectedStato);
            libriFiltrati = libriFiltrati.stream()
                    .filter(libro -> libro.getStatoLettura() == statoDaFiltrare)
                    .collect(Collectors.toList());
        }

        //filtro per Valutazione
        String selectedValutazione = (String) valutazioneFiltraComboBox.getSelectedItem();
        if (selectedValutazione != null && !selectedValutazione.equals("Tutte")) {
            libriFiltrati = libriFiltrati.stream()
                    .filter(libro -> libro.getValutazione() != null &&
                            libro.getValutazione().getDescrizione().equals(selectedValutazione))
                    .collect(Collectors.toList());
        }

        //Ricerca testuale
        String searchText = cercaField.getText().trim();
        if (!searchText.isEmpty()) {
            String lowerCaseSearchText = searchText.toLowerCase();
            libriFiltrati = libriFiltrati.stream()
                    .filter(libro ->
                            libro.getTitolo().toLowerCase().contains(lowerCaseSearchText) ||
                                    libro.getAutore().toLowerCase().contains(lowerCaseSearchText) ||
                                    libro.getISBN().toLowerCase().contains(lowerCaseSearchText) ||
                                    libro.getGenere().toLowerCase().contains(lowerCaseSearchText)
                    )
                    .collect(Collectors.toList());
        }

        // Aggiorna tutte e due le viste con le opzioni di cerca e filtra selezionate
        tableView.aggiorna(libriFiltrati);
        listView.aggiorna(libriFiltrati);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibreriaGUI libreria = new LibreriaGUI();
            libreria.setVisible(true);
        });
    }

}