package controller;

import model.Mouvement;
import model.TypeMouvement;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MouvementController {

    // ================================
    // Ajouter un mouvement
    // ================================
    public static boolean ajouterMouvement(Mouvement m) {
        String sql = "INSERT INTO Mouvement(idArticle, idLocal, typeMouvement, quantite, dateMouvement, observation, idCommandeInterne, idCommandeExterne) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getIdArticle());
            ps.setInt(2, m.getIdLocal());
            ps.setString(3, m.getTypeMouvement().name()); // ENTREE ou SORTIE
            ps.setInt(4, m.getQuantite());
            ps.setTimestamp(5, m.getDateMouvement());
            ps.setString(6, m.getObservation());

            if (m.getIdCommandeInterne() != null)
                ps.setInt(7, m.getIdCommandeInterne());
            else
                ps.setNull(7, java.sql.Types.INTEGER);

            if (m.getIdCommandeExterne() != null)
                ps.setInt(8, m.getIdCommandeExterne());
            else
                ps.setNull(8, java.sql.Types.INTEGER);

            int res = ps.executeUpdate();



            return res > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }}







    // ================================
    // Lister tous les mouvements
    // ================================
    public static List<Mouvement> getAllMouvements() {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT m.*, a.reference FROM Mouvement m " +
                "JOIN Article a ON m.idArticle = a.idArticle " +
                "ORDER BY m.dateMouvement DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Mouvement m = new Mouvement();

                m.setIdMouvement(rs.getInt("idMouvement"));
                m.setIdArticle(rs.getInt("idArticle"));
                m.setNomArticle(rs.getString("reference")); // 🔥 IMPORTANT
                m.setIdLocal(rs.getInt("idLocal"));

                // TypeMouvement depuis la chaîne (ENTREE / SORTIE)
                String typeStr = rs.getString("typeMouvement");
                if (typeStr != null) {
                    m.setTypeMouvement(TypeMouvement.valueOf(typeStr));
                }

                m.setQuantite(rs.getInt("quantite"));
                m.setDateMouvement(rs.getTimestamp("dateMouvement"));
                m.setObservation(rs.getString("observation"));

                // Gestion des clés externes (peuvent être null)
                int idCommandeInterne = rs.getInt("idCommandeInterne");
                if (!rs.wasNull()) m.setIdCommandeInterne(idCommandeInterne);

                int idCommandeExterne = rs.getInt("idCommandeExterne");
                if (!rs.wasNull()) m.setIdCommandeExterne(idCommandeExterne);

                mouvements.add(m);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mouvements;
    }
    // ================================
    // Optionnel : lister mouvements par article et local
    // ================================
    public List<Mouvement> getMouvementsByArticleAndLocal(int idArticle, int idLocal) {
        List<Mouvement> list = new ArrayList<>();
        String sql = "SELECT * FROM Mouvement WHERE idArticle = ? AND idLocal = ? ORDER BY dateMouvement DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idArticle);
            pst.setInt(2, idLocal);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mouvement m = new Mouvement();

                    m.setIdMouvement(rs.getInt("idMouvement"));
                    m.setIdArticle(rs.getInt("idArticle"));
                    m.setIdLocal(rs.getInt("idLocal"));

                    String typeStr = rs.getString("typeMouvement");
                    if (typeStr != null) {
                        m.setTypeMouvement(TypeMouvement.valueOf(typeStr));
                    }

                    m.setQuantite(rs.getInt("quantite"));
                    m.setDateMouvement(rs.getTimestamp("dateMouvement"));
                    m.setObservation(rs.getString("observation"));

                    // Gestion des commandes internes/externe (nullable)
                    int idCommandeInterne = rs.getInt("idCommandeInterne");
                    if (!rs.wasNull()) m.setIdCommandeInterne(idCommandeInterne);

                    int idCommandeExterne = rs.getInt("idCommandeExterne");
                    if (!rs.wasNull()) m.setIdCommandeExterne(idCommandeExterne);

                    list.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}