package controller;

import model.Utilisateur;
import model.Utilisateur;
import util.DBConnection;
import util.DBConnection;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthController {

    private Utilisateur utilisateurCourant = null;

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean login(String username, String password) {
        String hashedPassword = hashPassword(password);
        String query = "SELECT * FROM utilisateurs WHERE username = ? AND password = ? AND actif = true";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                utilisateurCourant = new Utilisateur();
                utilisateurCourant.setIdUtilisateur(rs.getInt("id_utilisateur"));
                utilisateurCourant.setUsername(rs.getString("username"));
                utilisateurCourant.setRole(rs.getString("role"));
                utilisateurCourant.setNomComplet(rs.getString("nom_complet"));
                utilisateurCourant.setEmail(rs.getString("email"));
                utilisateurCourant.setActif(rs.getBoolean("actif"));
                updateLastLogin(utilisateurCourant.getIdUtilisateur());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean createUser(Utilisateur user) {
        String hashedPassword = hashPassword(user.getPassword());
        String query = "INSERT INTO utilisateurs (username, password, role, nom_complet, email, actif) VALUES (?, ?, ?, ?, ?, true)";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getRole() != null ? user.getRole() : "USER");
            pstmt.setString(4, user.getNomComplet());
            pstmt.setString(5, user.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void updateLastLogin(int userId) {
        String query = "UPDATE utilisateurs SET dernier_login = CURRENT_TIMESTAMP WHERE id_utilisateur = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void logout() {
        utilisateurCourant = null;
    }
    public boolean isLoggedIn() {
        return utilisateurCourant != null;
    }
    public Utilisateur getCurrentUser() {
        return utilisateurCourant;
    }
    public boolean isAdmin() {
        return utilisateurCourant != null && "ADMIN".equals(utilisateurCourant.getRole());
    }
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String hashedOld = hashPassword(oldPassword);
        String hashedNew = hashPassword(newPassword);
        String checkQuery = "SELECT * FROM utilisateurs WHERE id_utilisateur = ? AND password = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(checkQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, hashedOld);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Mettre à jour le mot de passe
                String updateQuery = "UPDATE utilisateurs SET password = ? WHERE id_utilisateur = ?";
                try (PreparedStatement pstmt2 = DBConnection.getConnection().prepareStatement(updateQuery)) {
                    pstmt2.setString(1, hashedNew);
                    pstmt2.setInt(2, userId);
                    return pstmt2.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}