package util; // ou controller

import java.sql.Connection;
import java.sql.SQLException;
import util.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion PostgreSQL réussie !");
            } else {
                System.out.println("Connexion échouée !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}