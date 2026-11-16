package controller;

import model.Fournisseur;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurController {

    // Ajouter un fournisseur
    public boolean ajouterFournisseur(Fournisseur f) {
        String sql = "INSERT INTO Fournisseur (nomFournisseur, adresse, telephone, email, contactPrincipal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, f.getNomFournisseur());
            pst.setString(2, f.getAdresse());
            pst.setString(3, f.getTelephone());
            pst.setString(4, f.getEmail());
            pst.setString(5, f.getContactPrincipal());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Modifier un fournisseur
    public boolean modifierFournisseur(Fournisseur f) {
        String sql = "UPDATE Fournisseur SET nomFournisseur=?, adresse=?, telephone=?, email=?, contactPrincipal=? WHERE idFournisseur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, f.getNomFournisseur());
            pst.setString(2, f.getAdresse());
            pst.setString(3, f.getTelephone());
            pst.setString(4, f.getEmail());
            pst.setString(5, f.getContactPrincipal());
            pst.setInt(6, f.getIdFournisseur());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un fournisseur
    public boolean supprimerFournisseur(int id) {
        String sql = "DELETE FROM fournisseur WHERE idfournisseur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lister tous les fournisseurs

    public List<Fournisseur> getAllFournisseurs() {
        List<Fournisseur> list = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setIdFournisseur(rs.getInt("idFournisseur"));
                f.setNomFournisseur(rs.getString("nomFournisseur"));
                f.setAdresse(rs.getString("adresse"));
                f.setTelephone(rs.getString("telephone"));
                f.setEmail(rs.getString("email"));
                f.setContactPrincipal(rs.getString("contactPrincipal"));
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Recherche dynamique
    public List<Fournisseur> rechercherFournisseur(String motCle) {
        List<Fournisseur> list = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur WHERE nomFournisseur ILIKE ? OR adresse ILIKE ? OR email ILIKE ? OR contactPrincipal ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            String mc = "%" + motCle + "%";
            pst.setString(1, mc);
            pst.setString(2, mc);
            pst.setString(3, mc);
            pst.setString(4, mc);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setIdFournisseur(rs.getInt("idFournisseur"));
                f.setNomFournisseur(rs.getString("nomFournisseur"));
                f.setAdresse(rs.getString("adresse"));
                f.setTelephone(rs.getString("telephone"));
                f.setEmail(rs.getString("email"));
                f.setContactPrincipal(rs.getString("contactPrincipal"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}