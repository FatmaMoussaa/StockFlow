package controller;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeExterneController {

    public boolean ajouterCommande(CommandeExterne commande) {
        String sqlCommande = "INSERT INTO CommandeExterne (idFournisseur, montant, statut) VALUES (?, ?, ?)";
        String sqlLigne = "INSERT INTO LigneCommande (idCommandeExterne, idArticle, idLocal, quantite) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement pst = conn.prepareStatement(sqlCommande, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, commande.getIdFournisseur());
            pst.setDouble(2, commande.getMontant());
            pst.setString(3, commande.getStatut().toString());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                commande.setIdCommande(id);

                for (LigneCommande l : commande.getLignesCommande()) {
                    PreparedStatement pstL = conn.prepareStatement(sqlLigne);
                    pstL.setInt(1, id);
                    pstL.setInt(2, l.getIdArticle());
                    pstL.setInt(3, l.getIdLocal());
                    pstL.setInt(4, l.getQuantite());
                    pstL.executeUpdate();
                    pstL.close();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<CommandeExterne> getAllCommandes() {
        List<CommandeExterne> list = new ArrayList<>();
        String sql = "SELECT * FROM CommandeExterne";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CommandeExterne c = new CommandeExterne();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setIdFournisseur(rs.getInt("idFournisseur"));
                c.setMontant(rs.getDouble("montant"));
                c.setStatut(StatutCommande.valueOf(rs.getString("statut")));
                c.setLignesCommande(getLignesCommande(c.getIdCommande()));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public CommandeExterne getCommandeById(int id) {
        CommandeExterne c = null;

        String sql = "SELECT * FROM CommandeExterne WHERE idCommande=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                c = new CommandeExterne();
                c.setIdCommande(id);
                c.setIdFournisseur(rs.getInt("idFournisseur"));
                c.setMontant(rs.getDouble("montant"));
                c.setStatut(StatutCommande.valueOf(rs.getString("statut")));

                c.setLignesCommande(getLignesCommande(id)); // 🔥 CRUCIAL
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
    public boolean modifierCommande(CommandeExterne c) {

        String update = "UPDATE CommandeExterne SET idFournisseur=?, montant=?, statut=? WHERE idCommande=?";
        String delete = "DELETE FROM LigneCommande WHERE idCommandeExterne=?";
        String insert = "INSERT INTO LigneCommande (idCommandeExterne, idArticle, idLocal, quantite) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pst = conn.prepareStatement(update);
            pst.setInt(1, c.getIdFournisseur());
            pst.setDouble(2, c.getMontant());
            pst.setString(3, c.getStatut().toString());
            pst.setInt(4, c.getIdCommande());
            pst.executeUpdate();
            pst = conn.prepareStatement(delete);
            pst.setInt(1, c.getIdCommande());
            pst.executeUpdate();
            pst = conn.prepareStatement(insert);
            for (LigneCommande l : c.getLignesCommande()) {

                pst.setInt(1, c.getIdCommande());
                pst.setInt(2, l.getIdArticle());
                pst.setInt(3, l.getIdLocal());
                pst.setInt(4, l.getQuantite());

                pst.addBatch();
            }
            pst.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    public boolean supprimerCommande(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement pst = conn.prepareStatement("DELETE FROM LigneCommande WHERE idCommandeExterne=?");
            pst.setInt(1, id);
            pst.executeUpdate();

            pst = conn.prepareStatement("DELETE FROM CommandeExterne WHERE idCommande=?");
            pst.setInt(1, id);
            pst.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<LigneCommande> getLignesCommande(int id) {
        List<LigneCommande> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM LigneCommande WHERE idCommandeExterne=?")) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                LigneCommande l = new LigneCommande();
                l.setIdArticle(rs.getInt("idArticle"));
                l.setIdLocal(rs.getInt("idLocal"));
                l.setQuantite(rs.getInt("quantite"));
                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}