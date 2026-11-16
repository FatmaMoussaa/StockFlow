package controller;

import model.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ControleController {


    public boolean traiterControleCommandeInterne(CommandeInterne commande, LigneCommandeInterne ligne, Controle controle) {
        try {

            if (!ajouterControle(controle)) return false;
            StockController.updateStock(ligne.getIdArticle(), ligne.getIdLocalSource(), -controle.getQuantiteControllee());

            StockController.updateStock(ligne.getIdArticle(), ligne.getIdLocalDestination(), controle.getQuantiteControllee());

            Mouvement sortie = new Mouvement(
                    ligne.getIdArticle(),
                    ligne.getIdLocalSource(),
                    TypeMouvement.SORTIE,
                    controle.getQuantiteControllee(),
                    new Timestamp(new Date().getTime()),
                    "Commande interne sortie vers local " + ligne.getIdLocalDestination(),
                    commande.getIdCommande(),
                    null
            );
            MouvementController.ajouterMouvement(sortie);
            Mouvement entree = new Mouvement(
                    ligne.getIdArticle(),
                    ligne.getIdLocalDestination(),
                    TypeMouvement.ENTREE,
                    controle.getQuantiteControllee(),
                    new Timestamp(new Date().getTime()),
                    "Commande interne entrée depuis local " + ligne.getIdLocalSource(),
                    commande.getIdCommande(),
                    null
            );
            MouvementController.ajouterMouvement(entree);
            TraceController.ajouterTrace(
                    "Commande Interne",
                    "Magasinier",
                    "Article " + ligne.getIdArticle() +
                            " transféré de local " + ligne.getIdLocalSource() +
                            " vers local " + ligne.getIdLocalDestination() +
                            " pour commande #" + commande.getIdCommande()
            );

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean ajouterControle(Controle c) {
        String sql = "INSERT INTO Controle(dateControle, quantiteControllee, etat, observation, idArticle, " +
                "idLocalSource, idLocalDestination, idCommandeInterne, idCommandeExterne) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var conn = util.DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, c.getDateControle());
            ps.setInt(2, c.getQuantiteControllee());
            ps.setString(3, c.getEtat().name());
            ps.setString(4, c.getObservation());
            ps.setInt(5, c.getIdArticle());

            if (c.getIdLocalSource() != null) ps.setInt(6, c.getIdLocalSource());
            else ps.setNull(6, java.sql.Types.INTEGER);

            if (c.getIdLocalDestination() != null) ps.setInt(7, c.getIdLocalDestination());
            else ps.setNull(7, java.sql.Types.INTEGER);

            if (c.getIdCommandeInterne() != null) ps.setInt(8, c.getIdCommandeInterne());
            else ps.setNull(8, java.sql.Types.INTEGER);

            if (c.getIdCommandeExterne() != null) ps.setInt(9, c.getIdCommandeExterne());
            else ps.setNull(9, java.sql.Types.INTEGER);

            int res = ps.executeUpdate();



            return res > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }











    public boolean traiterControleExterne(Controle controle) {
        try {

            if (!ajouterControle(controle)) return false;

            int quantite = controle.getQuantiteControllee();
            int idArticle = controle.getIdArticle();
            int idLocal = controle.getIdLocalDestination();
            StockController.updateStock(idArticle, idLocal, quantite);
            Mouvement entree = new Mouvement(
                    idArticle,
                    idLocal,
                    TypeMouvement.ENTREE,
                    quantite,
                    new Timestamp(new Date().getTime()),
                    "Réception commande externe",
                    null,
                    controle.getIdCommandeExterne()
            );
            MouvementController.ajouterMouvement(entree);



            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }










}



