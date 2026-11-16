package controller;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.Date;

public class Controle2Controller {

    private StockController stockCtrl = new StockController();
    private TraceController traceCtrl = new TraceController();

    public boolean traiterControleInterne(CommandeInterne commande, LigneCommandeInterne ligne, Controle2 controle) {
        String sqlInsert = "INSERT INTO Controle2(dateControle, quantite, etat, observation, idArticle, idLocalSource, idLocalDestination, idCommandeInterne) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setTimestamp(1, new Timestamp(new Date().getTime()));
                ps.setInt(2, controle.getQuantite());
                ps.setString(3, controle.getEtat().name());
                ps.setString(4, controle.getObservation());
                ps.setInt(5, controle.getIdArticle());
                ps.setInt(6, controle.getIdLocalSource());
                ps.setInt(7, controle.getIdLocalDestination());
                ps.setInt(8, controle.getIdCommandeInterne());
                ps.executeUpdate();
            }
            int qte = controle.getQuantite();
            if (!stockCtrl.updateStock(controle.getIdArticle(), controle.getIdLocalSource(), -qte) ||
                    !stockCtrl.updateStock(controle.getIdArticle(), controle.getIdLocalDestination(), qte)) {
                conn.rollback();
                return false;
            }
            Mouvement sortie = new Mouvement(
                    controle.getIdArticle(),
                    controle.getIdLocalSource(),
                    TypeMouvement.SORTIE,
                    -qte,
                    new Timestamp(new Date().getTime()),
                    "Commande interne sortie vers local " + controle.getIdLocalDestination(),
                    controle.getIdCommandeInterne(),
                    null
            );
            Mouvement entree = new Mouvement(
                    controle.getIdArticle(),
                    controle.getIdLocalDestination(),
                    TypeMouvement.ENTREE,
                    qte,
                    new Timestamp(new Date().getTime()),
                    "Commande interne entrée depuis local " + controle.getIdLocalSource(),
                    controle.getIdCommandeInterne(),
                    null
            );

            if (!MouvementController.ajouterMouvement(sortie) || !MouvementController.ajouterMouvement(entree)) {
                conn.rollback();
                return false;
            }


            traceCtrl.ajouterTrace("Contrôle interne", "Magasinier",
                    "Commande interne #" + commande.getIdCommande() + " | Article #" + ligne.getIdArticle() + " contrôlé");

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}