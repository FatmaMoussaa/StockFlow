package controller;

import model.Trace;
import util.DBConnection;
import java.util.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraceController {

    public List<Trace> getAllTraces() {
        List<Trace> traces = new ArrayList<>();
        String sql = "SELECT * FROM trace ORDER BY dateoperation ";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trace t = new Trace();
                t.setIdTrace(rs.getInt("idtrace"));
                t.setAction(rs.getString("typeoperation"));
                t.setDateAction(rs.getString("dateoperation"));
                t.setUtilisateur(rs.getString("utilisateur"));
                traces.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return traces;
    }
    public static boolean ajouterTrace(String typeOperation, String utilisateur, String details) {
        String sql = "INSERT INTO Trace(dateOperation, typeOperation, utilisateur, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(2, typeOperation);
            ps.setString(3, utilisateur);
            ps.setString(4, details);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static boolean ajouterTrace2(String typeOperation, String utilisateur, String details, Connection conn) throws SQLException {
        String sql = "INSERT INTO Trace(dateOperation, typeOperation, utilisateur, details) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(2, typeOperation);
            ps.setString(3, utilisateur);
            ps.setString(4, details);

            return ps.executeUpdate() > 0;
        }
    }
}