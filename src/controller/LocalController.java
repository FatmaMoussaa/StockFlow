package controller;

import model.Local;
import model.TypeLocal; // ENUM Java
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalController {

    // ===== AJOUTER =====
    public boolean ajouterLocal(Local l) {
        String sql = "INSERT INTO Local(nomLocal, typeLocal, capacite, adresse) VALUES (?, ?::typelocal, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, l.getNomLocal());
            pst.setString(2, l.getTypeLocal().name()); // Enum -> String exact
            pst.setInt(3, l.getCapacite());
            pst.setString(4, l.getAdresse());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== MODIFIER =====
    public boolean modifierLocal(Local l) {
        String sql = "UPDATE Local SET nomLocal=?, typeLocal=?::typelocal, capacite=?, adresse=? WHERE idLocal=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, l.getNomLocal());
            pst.setString(2, l.getTypeLocal().name());
            pst.setInt(3, l.getCapacite());
            pst.setString(4, l.getAdresse());
            pst.setInt(5, l.getIdLocal());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== SUPPRIMER =====
    public boolean supprimerLocal(int id) {
        String sql = "DELETE FROM Local WHERE idLocal=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== LISTER TOUS =====
    public List<Local> getAllLocaux() {
        List<Local> list = new ArrayList<>();
        String sql = "SELECT * FROM Local ORDER BY idLocal";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Local l = new Local();
                l.setIdLocal(rs.getInt("idLocal"));
                l.setNomLocal(rs.getString("nomLocal"));
                l.setTypeLocal(TypeLocal.valueOf(rs.getString("typeLocal"))); // String -> ENUM
                l.setCapacite(rs.getInt("capacite"));
                l.setAdresse(rs.getString("adresse"));
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== RECHERCHE =====
    public List<Local> rechercherLocal(String motCle) {
        List<Local> list = new ArrayList<>();
        String sql = "SELECT * FROM Local WHERE nomLocal ILIKE ? OR typeLocal::text ILIKE ? OR adresse ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + motCle + "%");
            pst.setString(2, "%" + motCle + "%");
            pst.setString(3, "%" + motCle + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Local l = new Local();
                l.setIdLocal(rs.getInt("idLocal"));
                l.setNomLocal(rs.getString("nomLocal"));
                l.setTypeLocal(TypeLocal.valueOf(rs.getString("typeLocal")));
                l.setCapacite(rs.getInt("capacite"));
                l.setAdresse(rs.getString("adresse"));
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Local getLocalById(int id) {
        for (Local l : getAllLocaux()) { // récupère tous les locaux
            if (l.getIdLocal() == id) {
                return l;
            }
        }
        return null;
    }
}