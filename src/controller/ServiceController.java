package controller;

import model.Service;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {

    // Ajouter
    public boolean ajouterService(Service s){
        String sql = "INSERT INTO Service(nomService, typeService, responsable) VALUES (?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, s.getNomService());
            pst.setString(2, s.getTypeService());
            pst.setString(3, s.getResponsable());

            return pst.executeUpdate() > 0;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Modifier
    public boolean modifierService(Service s){
        String sql = "UPDATE Service SET nomService=?, typeService=?, responsable=? WHERE idService=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, s.getNomService());
            pst.setString(2, s.getTypeService());
            pst.setString(3, s.getResponsable());
            pst.setInt(4, s.getIdService());

            return pst.executeUpdate() > 0;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer
    public boolean supprimerService(int id){
        String sql = "DELETE FROM Service WHERE idService=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Lister tous
    public List<Service> getAllServices(){
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Service";

        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()){
                Service s = new Service();
                s.setIdService(rs.getInt("idService"));
                s.setNomService(rs.getString("nomService"));
                s.setTypeService(rs.getString("typeService"));
                s.setResponsable(rs.getString("responsable"));
                list.add(s);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    // Recherche
    public List<Service> rechercherService(String motCle){
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Service WHERE nomService ILIKE ? OR typeService ILIKE ? OR responsable ILIKE ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + motCle + "%");
            pst.setString(2, "%" + motCle + "%");
            pst.setString(3, "%" + motCle + "%");

            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Service s = new Service();
                s.setIdService(rs.getInt("idService"));
                s.setNomService(rs.getString("nomService"));
                s.setTypeService(rs.getString("typeService"));
                s.setResponsable(rs.getString("responsable"));
                list.add(s);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}