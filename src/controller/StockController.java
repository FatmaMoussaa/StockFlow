package controller;

import model.*;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static model.TypeMouvement.*;

public class StockController {
    public boolean miseAJourStockCommandeExterne(CommandeExterne commande) {
        boolean success = false;

        String updateStock = "UPDATE Stock " +
                "SET quantiteActuelle = quantiteActuelle + ?, " +
                "    quantiteReservee = GREATEST(quantiteReservee - ?, 0) " +
                "WHERE idArticle = ? AND idLocal = ?";

        String insertStockIfMissing = "INSERT INTO Stock (idArticle, idLocal, quantiteActuelle, quantiteReservee) VALUES (?, ?, ?, 0)";

        String insertMouvement = "INSERT INTO Mouvement " +
                "(idArticle, idLocal, typeMouvement, quantite, dateMouvement, observation, idcommandeinterne) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement pstUpdate = conn.prepareStatement(updateStock);
                    PreparedStatement pstInsertStock = conn.prepareStatement(insertStockIfMissing);
                    PreparedStatement pstMouvement = conn.prepareStatement(insertMouvement)
            ) {

                for (LigneCommande ligne : commande.getLignesCommande()) {

                    int qte = ligne.getQuantite();

                    // 1️⃣ Mise à jour du stock
                    pstUpdate.setInt(1, qte);
                    pstUpdate.setInt(2, qte);
                    pstUpdate.setInt(3, ligne.getIdArticle());
                    pstUpdate.setInt(4, ligne.getIdLocal());
                    int updated = pstUpdate.executeUpdate();

                    // 2️⃣ Si le stock n'existe pas → on le crée
                    if (updated == 0) {
                        pstInsertStock.setInt(1, ligne.getIdArticle());
                        pstInsertStock.setInt(2, ligne.getIdLocal());
                        pstInsertStock.setInt(3, qte);
                        pstInsertStock.executeUpdate();
                    }

                    // 3️⃣ Ajouter mouvement ENTREE
                    pstMouvement.setInt(1, ligne.getIdArticle());
                    pstMouvement.setInt(2, ligne.getIdLocal());
                    pstMouvement.setString(3, "ENTREE");
                    pstMouvement.setInt(4, qte);
                    pstMouvement.setString(5, "Commande Externe ID " + commande.getIdCommande());
                    pstMouvement.setNull(6, java.sql.Types.INTEGER); // ✅ idcommandeinterne = NULL
                    pstMouvement.executeUpdate();
                }

                // 4️⃣ Ajouter trace
                TraceController tc = new TraceController();
                tc.ajouterTrace(
                        "Réception Commande Externe",
                        "Magasinier",
                        "Commande ID " + commande.getIdCommande() + " validée"
                );

                conn.commit();
                success = true;

            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    // ========== GET / UPDATE STOCK ==========

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













    public static boolean updateStock(int idArticle, int idLocal, int delta) throws SQLException {
        String check = "SELECT quantiteActuelle FROM Stock WHERE idArticle = ? AND idLocal = ?";
        String insert = "INSERT INTO Stock(idArticle, idLocal, quantiteActuelle) VALUES (?, ?, ?)";
        String update = "UPDATE Stock SET quantiteActuelle = quantiteActuelle + ? WHERE idArticle=? AND idLocal=?";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstCheck = conn.prepareStatement(check);
            pstCheck.setInt(1, idArticle);
            pstCheck.setInt(2, idLocal);
            ResultSet rs = pstCheck.executeQuery();
            if (rs.next()) {
                // ligne existe → update
                PreparedStatement pstUpdate = conn.prepareStatement(update);
                pstUpdate.setInt(1, delta);
                pstUpdate.setInt(2, idArticle);
                pstUpdate.setInt(3, idLocal);
                return pstUpdate.executeUpdate() > 0;
            } else {
                // ligne n'existe pas → insert
                PreparedStatement pstInsert = conn.prepareStatement(insert);
                pstInsert.setInt(1, idArticle);
                pstInsert.setInt(2, idLocal);
                pstInsert.setInt(3, delta);
                return pstInsert.executeUpdate() > 0;
            }
        }
    }




    public static boolean updateStock2(int idArticle, int idLocal, int delta, Connection conn) throws SQLException {
        String check = "SELECT quantiteActuelle FROM Stock WHERE idArticle = ? AND idLocal = ?";
        String insert = "INSERT INTO Stock(idArticle, idLocal, quantiteActuelle) VALUES (?, ?, ?)";
        String update = "UPDATE Stock SET quantiteActuelle = quantiteActuelle + ? WHERE idArticle=? AND idLocal=?";

        try (PreparedStatement pstCheck = conn.prepareStatement(check)) {
            pstCheck.setInt(1, idArticle);
            pstCheck.setInt(2, idLocal);
            ResultSet rs = pstCheck.executeQuery();
            if (rs.next()) {
                try (PreparedStatement pstUpdate = conn.prepareStatement(update)) {
                    pstUpdate.setInt(1, delta);
                    pstUpdate.setInt(2, idArticle);
                    pstUpdate.setInt(3, idLocal);
                    return pstUpdate.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement pstInsert = conn.prepareStatement(insert)) {
                    pstInsert.setInt(1, idArticle);
                    pstInsert.setInt(2, idLocal);
                    pstInsert.setInt(3, delta);
                    return pstInsert.executeUpdate() > 0;
                }
            }
        }
    }


    public static boolean addMouvement2(Mouvement m, Connection conn) throws SQLException {
        String sql = "INSERT INTO Mouvement(idArticle, idLocal, typeMouvement, quantite, dateMouvement, observation, idCommandeInterne, idCommandeExterne) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getIdArticle());
            ps.setInt(2, m.getIdLocal());
            ps.setString(3, m.getTypeMouvement().name());
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

            return ps.executeUpdate() > 0;
        }
    }




    public void updateStatutCommandeInterne(Connection conn, int idCommande, StatutCommande statut) throws SQLException {
        String sql = "UPDATE CommandeInterne SET statut=? WHERE idCommande=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, statut.toString());
            pst.setInt(2, idCommande);
            pst.executeUpdate();
        }
    }






    public static int getQuantiteActuelle(int idArticle, int idLocal) throws SQLException {
        String sql = "SELECT quantiteActuelle FROM Stock WHERE idArticle = ? AND idLocal = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idArticle);
            pst.setInt(2, idLocal);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantiteActuelle");
            } else {
                // Si aucune ligne existante, le stock est considéré à 0
                return 0;
            }
        }
    }




    public static int getQuantiteActuelle2(int idArticle, int idLocal, Connection conn) throws SQLException {
        String sql = "SELECT quantiteActuelle FROM Stock WHERE idArticle = ? AND idLocal = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idArticle);
            pst.setInt(2, idLocal);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantiteActuelle");
            } else {
                return 0;
            }
        }
    }
    public boolean addMouvement(Mouvement m) throws SQLException {
        String sql = "INSERT INTO Mouvement " +
                "(idArticle, idLocal, quantite, typeMouvement, dateMouvement, observation, idcommandeinterne) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, m.getIdArticle());
            pst.setInt(2, m.getIdLocal());
            pst.setInt(3, m.getQuantite());
            pst.setString(4, setString(m.getTypeMouvement()));
            pst.setString(5, m.getObservation());

            // ✅ Ici on utilise l'ID commande interne et on gère le cas null
            if (m.getIdCommandeInterne() != null) {
                pst.setInt(6, m.getIdCommandeInterne());
            } else {
                pst.setNull(6, java.sql.Types.INTEGER);
            }

            return pst.executeUpdate() > 0;
        }
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

















    private String setString(TypeMouvement typeMouvement) {
        return typeMouvement.toString();
    }

    // ========== TRANSFERT COMMANDE INTERNE ==========
    public void miseAJourStockCommandeInterne(CommandeInterne commande) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            for (LigneCommandeInterne ligne : commande.getLignesCommande()) {
                int stock = getQuantiteActuelle(ligne.getIdArticle(), ligne.getIdLocalSource());
                if (stock < ligne.getQuantite()) throw new SQLException("Stock insuffisant !");

                // Sortie
                updateStock(ligne.getIdArticle(), ligne.getIdLocalSource(), -ligne.getQuantite());
                // Entrée
                updateStock(ligne.getIdArticle(), ligne.getIdLocalDestination(), ligne.getQuantite());

                // Mouvement Sortie
                Mouvement mSortie = new Mouvement();
                mSortie.setIdArticle(ligne.getIdArticle());
                mSortie.setIdLocal(ligne.getIdLocalSource());
                mSortie.setQuantite(-ligne.getQuantite());
                mSortie.setTypeMouvement(SORTIE);
                mSortie.setObservation("Vers local " + ligne.getIdLocalDestination());
                mSortie.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mSortie);

                // Mouvement Entrée
                Mouvement mEntree = new Mouvement();
                mEntree.setIdArticle(ligne.getIdArticle());
                mEntree.setIdLocal(ligne.getIdLocalDestination());
                mEntree.setQuantite(ligne.getQuantite());
                mEntree.setTypeMouvement(ENTREE);
                mEntree.setObservation("Depuis local " + ligne.getIdLocalSource());
                mEntree.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mEntree);
            }

            new TraceController().ajouterTrace(
                    "Transfert interne",
                    "Magasinier",
                    "Commande interne ID " + commande.getIdCommande() + " transférée"
            );
            conn.commit();
        }
    }

    // ========== RETOUR COMMANDE INTERNE ==========








    public boolean retourStockCommandeInterne(CommandeInterne commande) {
        boolean success = true;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            for (LigneCommandeInterne ligne : commande.getLignesCommande()) {
                int stockDest = getQuantiteActuelle(ligne.getIdArticle(), ligne.getIdLocalDestination());
                if (stockDest < ligne.getQuantite()) {
                    conn.rollback();
                    return false;
                }

                updateStock(ligne.getIdArticle(), ligne.getIdLocalDestination(), -ligne.getQuantite());
                updateStock(ligne.getIdArticle(), ligne.getIdLocalSource(), ligne.getQuantite());

                Mouvement mSortie = new Mouvement();
                mSortie.setIdArticle(ligne.getIdArticle());
                mSortie.setIdLocal(ligne.getIdLocalDestination());
                mSortie.setQuantite(-ligne.getQuantite());
                mSortie.setTypeMouvement(RETOUR);
                mSortie.setObservation("Retour commande interne ID " + commande.getIdCommande());
                mSortie.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mSortie);

                Mouvement mEntree = new Mouvement();
                mEntree.setIdArticle(ligne.getIdArticle());
                mEntree.setIdLocal(ligne.getIdLocalSource());
                mEntree.setQuantite(ligne.getQuantite());
                mEntree.setTypeMouvement(RETOUR);
                mEntree.setObservation("Retour commande interne ID " + commande.getIdCommande());
                mEntree.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mEntree);
            }
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }












    public boolean validerCommandeInterne(CommandeInterne commande) {
        boolean success = false;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (LigneCommandeInterne ligne : commande.getLignesCommande()) {

                int stockSource = getQuantiteActuelle(ligne.getIdArticle(), ligne.getIdLocalSource());
                if (stockSource < ligne.getQuantite()) {
                    conn.rollback();
                    System.err.println("Stock insuffisant pour l'article ID " + ligne.getIdArticle());
                    return false;
                }

                // Sortie du stock source
                updateStock(ligne.getIdArticle(), ligne.getIdLocalSource(), -ligne.getQuantite());

                // Entrée dans le stock destination
                updateStock(ligne.getIdArticle(), ligne.getIdLocalDestination(), ligne.getQuantite());

                // Mouvement Sortie
                Mouvement mSortie = new Mouvement();
                mSortie.setIdArticle(ligne.getIdArticle());
                mSortie.setIdLocal(ligne.getIdLocalSource());
                mSortie.setQuantite(-ligne.getQuantite());
                mSortie.setTypeMouvement(SORTIE);
                mSortie.setObservation("Transfert vers local " + ligne.getIdLocalDestination());
                mSortie.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mSortie);

                // Mouvement Entrée
                Mouvement mEntree = new Mouvement();
                mEntree.setIdArticle(ligne.getIdArticle());
                mEntree.setIdLocal(ligne.getIdLocalDestination());
                mEntree.setQuantite(ligne.getQuantite());
                mEntree.setTypeMouvement(ENTREE);
                mEntree.setObservation("Transfert depuis local " + ligne.getIdLocalSource());
                mEntree.setIdCommandeInterne(commande.getIdCommande());
                addMouvement(mEntree);
            }
            String sqlUpdate = "UPDATE CommandeInterne SET statut=? WHERE idCommande=?";
            try (PreparedStatement pst = conn.prepareStatement(sqlUpdate)) {
                pst.setString(1, StatutCommande.VALIDEE.toString());
                pst.setInt(2, commande.getIdCommande());
                pst.executeUpdate();
            }


            // Ajouter Trace
            TraceController tc = new TraceController();
            tc.ajouterTrace(
                    "Validation Commande Interne",
                    "Magasinier",
                    "Commande interne ID " + commande.getIdCommande() + " validée"
            );

            conn.commit();
            success = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            success = false;
        }

        return success;
    }


















    public List<Stock> getStockGlobal() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.idStock, s.idArticle, a.reference AS nomArticle, s.idLocal, l.nomLocal, s.quantiteActuelle " +
                "FROM Stock s " +
                "JOIN Article a ON s.idArticle = a.idArticle " +
                "JOIN Local l ON s.idLocal = l.idLocal " +
                "ORDER BY a.reference";

        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.Stock stock = new model.Stock();

                model.Article article = new model.Article();
                article.setIdArticle(rs.getInt("idArticle"));
                article.setReference(rs.getString("nomArticle"));
                stock.setArticle(article);

                model.Local local = new model.Local();
                local.setIdLocal(rs.getInt("idLocal"));
                local.setNomLocal(rs.getString("nomLocal"));
                stock.setLocal(local);

                stock.setIdStock(rs.getInt("idStock"));
                stock.setQuantiteActuelle(rs.getInt("quantiteActuelle"));

                stocks.add(stock);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return stocks;
    }
}