package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeInterneController {
    public boolean ajouterCommandeInterne(CommandeInterne cmd) {
        String sqlCommande = "INSERT INTO CommandeInterne (idService, statut) VALUES (?, ?)";
        String sqlLigne = "INSERT INTO LigneCommandeInterne (idCommandeinterne, idArticle, idLocalSource, idLocalDestination, quantite) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstCmd = conn.prepareStatement(sqlCommande, Statement.RETURN_GENERATED_KEYS)) {
                pstCmd.setInt(1, cmd.getIdService());
                pstCmd.setString(2, cmd.getStatut().name());
                pstCmd.executeUpdate();

                ResultSet rs = pstCmd.getGeneratedKeys();
                if (rs.next()) {
                    cmd.setIdCommande(rs.getInt(1));
                } else {
                    conn.rollback();
                    return false;
                }
            }
            try (PreparedStatement pstLigne = conn.prepareStatement(sqlLigne)) {
                for (LigneCommandeInterne ligne : cmd.getLignesCommande()) {
                    pstLigne.setInt(1, cmd.getIdCommande());
                    pstLigne.setInt(2, ligne.getIdArticle());
                    pstLigne.setInt(3, ligne.getIdLocalSource());
                    pstLigne.setInt(4, ligne.getIdLocalDestination());
                    pstLigne.setInt(5, ligne.getQuantite());
                    pstLigne.addBatch();
                }
                pstLigne.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public List<CommandeInterne> getAllCommandesInternes() {
        List<CommandeInterne> list = new ArrayList<>();
        String sql = "SELECT * FROM CommandeInterne ORDER BY idCommande DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CommandeInterne c = new CommandeInterne();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setIdService(rs.getInt("idService"));
                c.setStatut(Enum.valueOf(model.StatutCommande.class, rs.getString("statut")));
                c.setLignesCommande(getLignesCommande(c.getIdCommande(), conn));

                list.add(c);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public boolean supprimerCommandeInterne(int idCommande) {
        String sqlLignes = "DELETE FROM LigneCommandeInterne WHERE idCommandeinterne=?";
        String sqlCommande = "DELETE FROM CommandeInterne WHERE idCommande=?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstL = conn.prepareStatement(sqlLignes)) {
                pstL.setInt(1, idCommande);
                pstL.executeUpdate();
            }

            try (PreparedStatement pstC = conn.prepareStatement(sqlCommande)) {
                pstC.setInt(1, idCommande);
                pstC.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public boolean modifierCommandeInterne(CommandeInterne c) {

        String update = "UPDATE CommandeInterne SET idService=?, statut=? WHERE idCommande=?";

        String delete = "DELETE FROM LigneCommandeInterne WHERE idCommandeinterne=?";

        String insert = "INSERT INTO LigneCommandeInterne (idCommandeinterne, idArticle, idLocalSource, idLocalDestination, quantite, retourEffectue) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pst = conn.prepareStatement(update);
            pst.setInt(1, c.getIdService());
            pst.setString(2, c.getStatut().toString());
            pst.setInt(3, c.getIdCommande());
            pst.executeUpdate();
            pst = conn.prepareStatement(delete);
            pst.setInt(1, c.getIdCommande());
            pst.executeUpdate();
            pst = conn.prepareStatement(insert);

            for (LigneCommandeInterne l : c.getLignesCommande()) {

                pst.setInt(1, c.getIdCommande());
                pst.setInt(2, l.getIdArticle());
                pst.setInt(3, l.getIdLocalSource());
                pst.setInt(4, l.getIdLocalDestination());
                pst.setInt(5, l.getQuantite());
                pst.setBoolean(6, l.isRetourEffectue());

                pst.addBatch();
            }

            pst.executeBatch();
            TraceController.ajouterTrace(
                    "Modification CI",
                    "Magasinier",
                    "Commande interne ID=" + c.getIdCommande()
            );

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

    private List<LigneCommandeInterne> getLignesCommande(int idCommande, Connection conn) throws SQLException {
        List<LigneCommandeInterne> lignes = new ArrayList<>();
        String sql = "SELECT * FROM LigneCommandeInterne WHERE idCommandeinterne=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idCommande);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    LigneCommandeInterne ligne = new LigneCommandeInterne();

                    ligne.setIdLigne(rs.getInt("idLigne")); //
                    ligne.setIdArticle(rs.getInt("idArticle"));
                    ligne.setIdLocalSource(rs.getInt("idLocalSource"));
                    ligne.setIdLocalDestination(rs.getInt("idLocalDestination"));
                    ligne.setQuantite(rs.getInt("quantite"));
                    ligne.setRetourEffectue(rs.getBoolean("retourEffectue")); // ✅ AJOUTE ÇA
                    lignes.add(ligne);
                }
            }
        }
        return lignes;
    }











    public CommandeInterne getCommandeById(int idCommande) {
        CommandeInterne commande = null;

        String sqlCommande = "SELECT * FROM CommandeInterne WHERE idCommande = ?";
        String sqlLignes = "SELECT * FROM LigneCommandeInterne WHERE idCommandeinterne = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstCommande = conn.prepareStatement(sqlCommande);
             PreparedStatement pstLignes = conn.prepareStatement(sqlLignes)) {
            pstCommande.setInt(1, idCommande);
            ResultSet rsC = pstCommande.executeQuery();
            if (rsC.next()) {
                commande = new CommandeInterne();
                commande.setIdCommande(rsC.getInt("idCommande"));
                commande.setIdService(rsC.getInt("idService"));
                commande.setStatut(StatutCommande.valueOf(rsC.getString("statut")));
            }

            if (commande != null) {
                pstLignes.setInt(1, idCommande);
                ResultSet rsL = pstLignes.executeQuery();
                List<LigneCommandeInterne> lignes = new ArrayList<>();
                while (rsL.next()) {
                    LigneCommandeInterne ligne = new LigneCommandeInterne();
                    ligne.setIdLigne(rsL.getInt("idLigne"));
                    ligne.setIdArticle(rsL.getInt("idArticle"));
                    ligne.setQuantite(rsL.getInt("quantite"));
                    ligne.setIdLocalDestination(rsL.getInt("idLocalDestination"));
                    lignes.add(ligne);
                }
                commande.setLignesCommande(lignes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commande;
    }
    public List<CommandeInterne> getAllCommandes() {
        List<CommandeInterne> commandes = new ArrayList<>();
        String sql = "SELECT * FROM CommandeInterne";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CommandeInterne c = new CommandeInterne();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setIdService(rs.getInt("idService"));
                c.setStatut(StatutCommande.valueOf(rs.getString("statut")));
                commandes.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

public boolean modifierCommande(CommandeInterne commande) {
    String sql = "UPDATE CommandeInterne SET statut = ? WHERE idCommande = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, commande.getStatut().name());
        pst.setInt(2, commande.getIdCommande());

        int updated = pst.executeUpdate();
        return updated > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}



    public boolean retournerCommandeInterne(CommandeInterne cmd) {
        String sqlUpdateStock = "UPDATE Stock SET quantiteactuelle = quantiteactuelle + ? WHERE idArticle=? AND idLocal=?";
        String sqlUpdateStockDest = "UPDATE Stock SET quantiteactuelle = quantiteactuelle - ? WHERE idArticle=? AND idLocal=?";
        String sqlInsertMouvement = "INSERT INTO Mouvement (idArticle, idLocal, typeMouvement, quantite, dateMouvement, idCommandeinterne,observation) VALUES (?, ?, ?, ?, NOW(), ?,?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (LigneCommandeInterne ligne : cmd.getLignesCommande()) {

                if (ligne.isRetourEffectue()) {
                    continue;
                }
                int articleId = ligne.getIdArticle();
                int qte = ligne.getQuantite();
                int localSource = ligne.getIdLocalSource();
                int localDest = ligne.getIdLocalDestination();
                try (PreparedStatement pstDest = conn.prepareStatement(sqlUpdateStockDest)) {
                    pstDest.setInt(1, qte);
                    pstDest.setInt(2, articleId);
                    pstDest.setInt(3, localDest);
                    pstDest.executeUpdate();
                }
                try (PreparedStatement pstSource = conn.prepareStatement(sqlUpdateStock)) {
                    pstSource.setInt(1, qte);
                    pstSource.setInt(2, articleId);
                    pstSource.setInt(3, localSource);
                    pstSource.executeUpdate();
                }


                try (PreparedStatement pstMouv = conn.prepareStatement(sqlInsertMouvement)) {
                    String observation = "Retour: article " + articleId
                            + " de local " + localSource
                            + " vers local " + localDest;

                    pstMouv.setInt(1, articleId);
                    pstMouv.setInt(2, localDest);
                    pstMouv.setString(3, "SORTIE");
                    pstMouv.setInt(4, qte);
                    pstMouv.setInt(5, cmd.getIdCommande());
                    pstMouv.setString(6, observation);
                    pstMouv.executeUpdate();


                    pstMouv.setInt(1, articleId);
                    pstMouv.setInt(2, localSource);
                    pstMouv.setString(3, "ENTREE");
                    pstMouv.setInt(4, qte);
                    pstMouv.setInt(5, cmd.getIdCommande());
                    pstMouv.setString(6, observation);
                    pstMouv.executeUpdate();
                }
                ligne.setRetourEffectue(true);

                String sqlUpdateRetour = "UPDATE LigneCommandeInterne SET retourEffectue = TRUE WHERE idLigne = ?";
                try (PreparedStatement pst = conn.prepareStatement(sqlUpdateRetour)) {
                    pst.setInt(1, ligne.getIdLigne());
                    pst.executeUpdate();
                }
            }

            boolean toutesRetournees = cmd.getLignesCommande().stream()
                    .allMatch(LigneCommandeInterne::isRetourEffectue);

            if (toutesRetournees) {
                String sqlUpdateStatut = "UPDATE CommandeInterne SET statut=? WHERE idCommande=?";
                try (PreparedStatement pst = conn.prepareStatement(sqlUpdateStatut)) {
                    pst.setString(1, "RETOURNEE"); // ou StatutCommande.RETOURNEE.toString() si tu as enum
                    pst.setInt(2, cmd.getIdCommande());
                    pst.executeUpdate();
                }
            }
            TraceController tc = new TraceController();
            tc.ajouterTrace("Retour CI", "Magasinier", "Commande ID " + cmd.getIdCommande());

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean retournerLigneCommande(LigneCommandeInterne ligne, int idCommande) {

        if (ligne.isRetourEffectue()) {
            System.out.println("Cette ligne est déjà retournée !");
            return false; // empêche double retour
        }
        String sqlUpdateStock = "UPDATE Stock SET quantiteactuelle = quantiteactuelle + ? WHERE idArticle=? AND idLocal=?";
        String sqlUpdateStockDest = "UPDATE Stock SET quantiteactuelle = quantiteactuelle - ? WHERE idArticle=? AND idLocal=?";
        String sqlInsertMouvement = "INSERT INTO Mouvement (idArticle, idLocal, typeMouvement, quantite, dateMouvement, idCommandeinterne, observation) VALUES (?, ?, ?, ?, NOW(), ?, ?)";
        String sqlUpdateRetour = "UPDATE LigneCommandeInterne SET retourEffectue = TRUE WHERE idLigne = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int articleId = ligne.getIdArticle();
            int qte = ligne.getQuantite();
            int localSource = ligne.getIdLocalSource();
            int localDest = ligne.getIdLocalDestination();
            try (PreparedStatement pst = conn.prepareStatement(sqlUpdateStockDest)) {
                pst.setInt(1, qte);
                pst.setInt(2, articleId);
                pst.setInt(3, localDest);
                pst.executeUpdate();
            }
            try (PreparedStatement pst = conn.prepareStatement(sqlUpdateStock)) {
                pst.setInt(1, qte);
                pst.setInt(2, articleId);
                pst.setInt(3, localSource);
                pst.executeUpdate();
            }
            try (PreparedStatement pst = conn.prepareStatement(sqlInsertMouvement)) {
                String observation = "Retour ligne CI ID " + idCommande;
                pst.setInt(1, articleId);
                pst.setInt(2, localDest);
                pst.setString(3, "SORTIE");
                pst.setInt(4, -qte);
                pst.setInt(5, idCommande);
                pst.setString(6, observation);
                pst.executeUpdate();
                pst.setInt(1, articleId);
                pst.setInt(2, localSource);
                pst.setString(3, "ENTREE");
                pst.setInt(4, qte);
                pst.setInt(5, idCommande);
                pst.setString(6, observation);
                pst.executeUpdate();
            }
            try (PreparedStatement pst = conn.prepareStatement(sqlUpdateRetour)) {
                pst.setInt(1, ligne.getIdLigne());
                pst.executeUpdate();
            }
            ligne.setRetourEffectue(true);

            String descriptionTrace = "Retour de la ligne CI ID " + idCommande
                    + ", article ID " + articleId
                    + ", qté " + qte;
            TraceController.ajouterTrace("Retour Ligne CI","Magasinier", "Retour lIGNE CI");

            conn.commit();
            System.out.println("Retour effectué avec succès !");
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }}











