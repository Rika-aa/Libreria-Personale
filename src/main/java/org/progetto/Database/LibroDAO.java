package org.progetto.Database;

import org.progetto.Model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private static final String JDBC_URL = "jdbc:sqlite:libreria.db"; //connessione sqlite db

    public LibroDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS libri ("+
                "isbn TEXT PRIMARY KEY,"+ //chiave primaria unica
                "titolo TEXT NOT NULL,"+
                "autore TEXT NOT NULL,"+
                "genere TEXT NOT NULL,"+
                "valutazione TEXT,"+ //descrizione valutazione
                "stato_lettura TEXT"+ //nome stato lettura
                ")";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql); //per eseguire create table statement
            System.out.println("Tabella creata.");
        } catch (SQLException e){
            System.out.println("Errore creazione tabella: " +e.getMessage());
        }
    }

    public boolean addLibro(Libro libro) {
        String sql = "INSERT INTO libri(isbn, titolo, autore, genere, valutazione, stato_lettura) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DriverManager.getConnection(JDBC_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, libro.getISBN());
            pstmt.setString(2, libro.getTitolo());
            pstmt.setString(3, libro.getAutore());
            pstmt.setString(4, libro.getGenere());
            pstmt.setString(5, libro.getValutazione() != null ? libro.getValutazione().getDescrizione() : null);
            pstmt.executeUpdate();
            System.out.println("Libro aggiunto: "+libro.getTitolo());
            return true;
        } catch (SQLException e) {
            System.out.println("Errore inserimento: " +e.getMessage());
            return false;
        }
    }

    public List<Libro> getAllLibri() {
        List<Libro> libri = new ArrayList<>();
        String sql = "SELECT isbn, titolo, autore, genere, valutazione, stato_lettura FROM libri";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titolo = rs.getString("titolo");
                String autore = rs.getString("autore");
                String genere = rs.getString("genere");
                String valutazioneStr = rs.getString("valutazione");
                String statoLetturaStr = rs.getString("stato_lettura");

                Libro libro = new Libro(titolo, autore, isbn, genere);

                if (valutazioneStr != null && !valutazioneStr.isEmpty()) {
                    for (Libro.Valutazione val : Libro.Valutazione.values()) {
                        if (val.getDescrizione().equals(valutazioneStr)) {
                            libro.setValutazione(val);
                            break;
                        }
                    }
                }
                if (statoLetturaStr != null && !statoLetturaStr.isEmpty()) {
                    libro.setStatoLettura(Libro.StatoLettura.valueOf(statoLetturaStr));
                }

                libri.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei libri: " + e.getMessage());
        }
        return libri;
    }

    public boolean updateLibro(Libro libro) {
        String sql = "UPDATE libri SET titolo = ?, autore = ?, genere = ?, valutazione = ?, stato_lettura = ? WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getTitolo());
            pstmt.setString(2, libro.getAutore());
            pstmt.setString(3, libro.getGenere());
            pstmt.setString(4, libro.getValutazione() != null ? libro.getValutazione().getDescrizione() : null);
            pstmt.setString(5, libro.getStatoLettura().name());
            pstmt.setString(6, libro.getISBN()); // Usiamo l'ISBN per trovare il libro da aggiornare
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento del libro: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLibro(String isbn) {
        String sql = "DELETE FROM libri WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("DEBUG DELETE: Tentativo di eliminare ISBN: " + isbn + ", Righe affette: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione del libro: " + e.getMessage());
            return false;
        }
    }

    public Libro getLibroIsbn(String isbn) {
        String sql = "SELECT isbn, titolo, autore, genere, valutazione, stato_lettura FROM libri WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String titolo = rs.getString("titolo");
                String autore = rs.getString("autore");
                String genere = rs.getString("genere");
                String valutazioneStr = rs.getString("valutazione");
                String statoLetturaStr = rs.getString("stato_lettura");

                Libro libro = new Libro(titolo, autore, isbn, genere);

                if (valutazioneStr != null && !valutazioneStr.isEmpty()) {
                    for (Libro.Valutazione val : Libro.Valutazione.values()) {
                        if (val.getDescrizione().equals(valutazioneStr)) {
                            libro.setValutazione(val);
                            break;
                        }
                    }
                }
                if (statoLetturaStr != null && !statoLetturaStr.isEmpty()) {
                    libro.setStatoLettura(Libro.StatoLettura.valueOf(statoLetturaStr));
                }
                return libro;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca del libro per ISBN: " + e.getMessage());
        }
        return null;
    }

    public void clearLibri() {
        String sql = "DELETE FROM libri";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tutti i libri eliminati dal database.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione di tutti i libri: " + e.getMessage());
        }
    }

}
