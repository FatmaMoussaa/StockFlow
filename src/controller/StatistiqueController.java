package controller;

import util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Statistique;

import java.sql.*;
import java.time.LocalDate;

public class StatistiqueController {

    // ===== Produit le plus consommé au global =====


    public ObservableList<Statistique> getProduitPlusConsommeGlobal(LocalDate debut, LocalDate fin) {

        ObservableList<Statistique> list = FXCollections.observableArrayList();

        String sql = """
        SELECT 
            a.reference AS nomArticle,
            a.type AS typeArticle,
            SUM(CASE WHEN m.typeMouvement='SORTIE' THEN m.quantite ELSE 0 END) AS quantiteTotale,
            COUNT(DISTINCT s.idService) AS nbServices
        FROM Article a
        LEFT JOIN Mouvement m ON a.idArticle = m.idArticle
        LEFT JOIN CommandeInterne c ON m.idCommandeInterne = c.idCommande
        LEFT JOIN Service s ON c.idService = s.idService
        WHERE m.dateMouvement BETWEEN ? AND ?
        GROUP BY a.reference, a.type
       ORDER BY nbServices DESC, quantiteTotale DESC
                   LIMIT 1
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, Timestamp.valueOf(debut.atStartOfDay()));
            pst.setTimestamp(2, Timestamp.valueOf(fin.atTime(23, 59, 59)));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                list.add(new Statistique(
                        rs.getString("nomArticle"),
                        rs.getString("typeArticle"),
                        rs.getInt("quantiteTotale"),
                        rs.getInt("nbServices"),
                        "Global"
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== Produit consommé par le plus grand nombre de services =====
    public ObservableList<Statistique> getProduitParNbServices(LocalDate debut, LocalDate fin) {
        ObservableList<Statistique> list = FXCollections.observableArrayList();

        String sql = """
            SELECT 
                a.reference AS nomArticle,
                a.type AS typeArticle,
                SUM(CASE WHEN m.typeMouvement='SORTIE' THEN m.quantite ELSE 0 END) AS quantiteTotale,
                COUNT(DISTINCT s.idService) AS nbServices
            FROM Article a
            LEFT JOIN Mouvement m ON a.idArticle = m.idArticle
            LEFT JOIN CommandeInterne c ON m.idCommandeInterne = c.idCommande
            LEFT JOIN Service s ON c.idService = s.idService
            WHERE m.dateMouvement BETWEEN ? AND ?
            GROUP BY a.reference, a.type
            ORDER BY nbServices DESC, quantiteTotale DESC
           
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, Timestamp.valueOf(debut.atStartOfDay()));
            pst.setTimestamp(2, Timestamp.valueOf(fin.atTime(23, 59, 59)));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Statistique s = new Statistique(
                        rs.getString("nomArticle"),
                        rs.getString("typeArticle"),
                        rs.getInt("quantiteTotale"),
                        rs.getInt("nbServices"),
                        "Par service"
                );
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}