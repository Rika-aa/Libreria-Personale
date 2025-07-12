import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.progetto.Database.LibroDAO;
import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Libro;
import org.progetto.Model.Memento.Memento;
import org.progetto.Model.Strategy.FiltroStatoLettura;
import org.progetto.Model.Strategy.FiltroValutazione;

@DisplayName("Test per GestoreLibreria e la persistenza")
class GestoreLibreriaTest {

    private GestoreLibreria gestore;
    private LibroDAO libroDAO;
    private static final String DB_FILE_NAME = "libreria.db"; // Nome del file del DB

    @BeforeEach
    void setUp() {
        deleteDatabaseFile();
        gestore = new GestoreLibreria();
        libroDAO = new LibroDAO();
    }

    //metodo clean up per ogni test
    private void deleteDatabaseFile() {
        File dbFile = new File(DB_FILE_NAME);
        if (dbFile.exists() && !dbFile.delete()) {
            fail("Impossibile eliminare il file del database per il test.");
        }
    }

    @Test
    @DisplayName("Test per aggiungere correttamente un libro al gestore e al database")
    void testAggiungiLibro() {
        Libro libro = new Libro("Il Nome della Rosa", "Umberto Eco", "978-8845249419", "Giallo Storico");
        libro.setValutazione(Libro.Valutazione.ECCELLENTE);
        libro.setStatoLettura(Libro.StatoLettura.DA_LEGGERE);

        gestore.aggiungiLibro(libro);

        // Verifica in memoria
        List<Libro> libriInMemoria = gestore.getLibri();
        assertEquals(1, libriInMemoria.size());
        assertTrue(libriInMemoria.contains(libro));

        // Verifica nel database
        Libro libroDalDB = libroDAO.getLibroIsbn(libro.getISBN());
        assertNotNull(libroDalDB);
        assertEquals(libro.getTitolo(), libroDalDB.getTitolo());
        assertEquals(libro.getAutore(), libroDalDB.getAutore());
        assertEquals(libro.getGenere(), libroDalDB.getGenere());
        assertEquals(libro.getValutazione(), libroDalDB.getValutazione());
        assertEquals(libro.getStatoLettura(), libroDalDB.getStatoLettura());
    }

    @Test
    @DisplayName("Test per eliminare correttamente un libro dal gestore e dal database")
    void testEliminaLibro() {
        Libro libro = new Libro("1984", "George Orwell", "978-0451524935", "Distopico");
        gestore.aggiungiLibro(libro); // Aggiunge per poter eliminare

        assertEquals(1, gestore.getLibri().size());
        assertNotNull(libroDAO.getLibroIsbn(libro.getISBN()));

        gestore.eliminaLibro(libro);

        // Verifica in memoria
        List<Libro> libriInMemoria = gestore.getLibri();
        assertEquals(0, libriInMemoria.size());
        assertFalse(libriInMemoria.contains(libro));

        // Verifica nel database
        assertNull(libroDAO.getLibroIsbn(libro.getISBN()));
    }

    @Test
    @DisplayName("Test modifica corretta di un libro nel gestore e nel database")
    void testModificaLibro() {
        Libro libroOriginale = new Libro("Dune", "Frank Herbert", "978-0441172719", "Fantascienza");
        libroOriginale.setStatoLettura(Libro.StatoLettura.DA_LEGGERE);
        gestore.aggiungiLibro(libroOriginale);

        Libro libroModificato = new Libro(libroOriginale.getTitolo(), libroOriginale.getAutore(), libroOriginale.getISBN(), "Fantascienza Classica");
        libroModificato.setValutazione(Libro.Valutazione.ECCELLENTE);
        libroModificato.setStatoLettura(Libro.StatoLettura.LETTO);

        gestore.modificaLibro(libroModificato);

        // Verifica in memoria
        List<Libro> libriInMemoria = gestore.getLibri();
        assertEquals(1, libriInMemoria.size());
        assertEquals(libroModificato, libriInMemoria.get(0)); // Richiede equals() in Libro

        // Verifica nel database
        Libro libroDalDB = libroDAO.getLibroIsbn(libroModificato.getISBN());
        assertNotNull(libroDalDB);
        assertEquals(libroModificato.getGenere(), libroDalDB.getGenere());
        assertEquals(libroModificato.getValutazione(), libroDalDB.getValutazione());
        assertEquals(libroModificato.getStatoLettura(), libroDalDB.getStatoLettura());
    }

    @Test
    @DisplayName("Test per ripristino con Memento e sincronizzazione con il database")
    void testMementoRestore() {
        Libro libro1 = new Libro("Libro Uno", "Autore Uno", "ISBN-001", "Genere A");
        Libro libro2 = new Libro("Libro Due", "Autore Due", "ISBN-002", "Genere B");
        gestore.aggiungiLibro(libro1);
        gestore.aggiungiLibro(libro2);

        // Salva lo stato (Memento)
        Memento statoIniziale = gestore.save();

        // Modifica lo stato attuale
        Libro libro3 = new Libro("Libro Tre", "Autore Tre", "ISBN-003", "Genere C");
        gestore.aggiungiLibro(libro3);
        gestore.eliminaLibro(libro1);

        assertEquals(2, gestore.getLibri().size());
        assertNull(libroDAO.getLibroIsbn(libro1.getISBN()));
        assertNotNull(libroDAO.getLibroIsbn(libro3.getISBN()));

        // Ripristina lo stato precedente
        gestore.restore(statoIniziale);

        // Verifica che lo stato in memoria sia quello ripristinato
        List<Libro> libriRipristinati = gestore.getLibri();
        assertEquals(2, libriRipristinati.size());
        assertTrue(libriRipristinati.contains(libro1));
        assertTrue(libriRipristinati.contains(libro2));
        assertFalse(libriRipristinati.contains(libro3));

        // Verifica che il database sia stato sincronizzato con lo stato ripristinato
        assertEquals(2, libroDAO.getAllLibri().size());
        assertNotNull(libroDAO.getLibroIsbn(libro1.getISBN()));
        assertNotNull(libroDAO.getLibroIsbn(libro2.getISBN()));
        assertNull(libroDAO.getLibroIsbn(libro3.getISBN()));
    }

    @Test
    @DisplayName("Test filtro per Valutazione")
    void testFiltroValutazione() {
        Libro libro = new Libro("Test", "Autore", "ISBN-123", "Genere");
        libro.setValutazione(Libro.Valutazione.BUONO);

        FiltroValutazione filtro = new FiltroValutazione(Libro.Valutazione.BUONO);
        assertTrue(filtro.filtra(libro));

        FiltroValutazione filtroFail = new FiltroValutazione(Libro.Valutazione.ECCELLENTE);
        assertFalse(filtroFail.filtra(libro));

        libro.setValutazione(null);
        assertFalse(filtro.filtra(libro));  // se il codice non gestisce null, potresti avere NullPointerException qui
    }

    @Test
    @DisplayName("Test filtro per Stato Lettura")
    void testFiltroStatoLettura() {
        Libro libro = new Libro("Test", "Autore", "ISBN-456", "Genere");
        libro.setStatoLettura(Libro.StatoLettura.LETTO);

        FiltroStatoLettura filtro = new FiltroStatoLettura(Libro.StatoLettura.LETTO);
        assertTrue(filtro.filtra(libro));

        FiltroStatoLettura filtroFail = new FiltroStatoLettura(Libro.StatoLettura.DA_LEGGERE);
        assertFalse(filtroFail.filtra(libro));

        libro.setStatoLettura(null);
        assertFalse(filtro.filtra(libro));
    }
}
