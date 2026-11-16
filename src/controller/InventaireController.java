package controller;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventaireController {

    private TraceController traceController = new TraceController();
    private StockController stockController = new StockController();

    public boolean ajouterInventaire(Inventaire inv) {
        boolean success = false;

        String sqlInv = "INSERT INTO Inventaire (dateInventaire, statut, notes) VALUES (?, ?, ?)";
        String sqlLigne = "INSERT INTO LigneInventaire (idInventaire, idArticle, idLocal, quantite) VALUES (?, ?, ?, ?)";
        String sqlMouvement = "INSERT INTO Mouvement (idArticle, idLocal, typeMouvement, quantite, dateMouvement, observation) " +
                "VALUES (?, ?, ?, ?, NOW(), ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Ajouter inventaire
            PreparedStatement pst = conn.prepareStatement(sqlInv, Statement.RETURN_GENERATED_KEYS);
            pst.setDate(1, new java.sql.Date(inv.getDateInventaire().getTime()));
            pst.setString(2, inv.getStatut().toString());
            pst.setString(3, inv.getNotes());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) inv.setIdInventaire(rs.getInt(1));
            else { conn.rollback(); return false; }
            pst.close();

            // 2️⃣ Ajouter lignes et ajuster le stock
            for (LigneInventaire ligne : inv.getLignes()) {
                // Ligne inventaire
                pst = conn.prepareStatement(sqlLigne);
                pst.setInt(1, inv.getIdInventaire());
                pst.setInt(2, ligne.getIdArticle());
                pst.setInt(3, ligne.getIdLocal());
                pst.setInt(4, ligne.getQuantite());
                pst.executeUpdate();
                pst.close();

                // Stock actuel
                int stockActuel = stockController.getQuantiteActuelle(ligne.getIdArticle(), ligne.getIdLocal());
                int diff = ligne.getQuantite() - stockActuel;

                if (diff != 0) {
                    // Mise à jour stock
                    String updateStock = "UPDATE Stock SET quantiteActuelle = ? WHERE idArticle=? AND idLocal=?";
                    try (PreparedStatement pstStock = conn.prepareStatement(updateStock)) {
                        pstStock.setInt(1, ligne.getQuantite());
                        pstStock.setInt(2, ligne.getIdArticle());
                        pstStock.setInt(3, ligne.getIdLocal());
                        pstStock.executeUpdate();
                    }

                    // 3️⃣ insertion mouvement AJUSTEMENT
                    try (PreparedStatement pstMouv = conn.prepareStatement(sqlMouvement)) {
                        pstMouv.setInt(1, ligne.getIdArticle());
                        pstMouv.setInt(2, ligne.getIdLocal());
                        pstMouv.setString(3, "AJUSTEMENT"); // ← important
                        pstMouv.setInt(4, Math.abs(diff));   // quantité ajustée
                        pstMouv.setString(5, "Ajustement Inventaire ID " + inv.getIdInventaire());
                        pstMouv.executeUpdate();
                    }

                    // Trace
                    traceController.ajouterTrace(
                            "Ajustement stock",
                            "Magasinier",
                            "Article ID " + ligne.getIdArticle() +
                                    " Local ID " + ligne.getIdLocal() +
                                    " ajusté de " + diff
                    );
                }
            }

            // 3️⃣ Commit final
            conn.commit();
            success = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return success;
    }
}