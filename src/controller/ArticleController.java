package controller;

import model.Article;
import model.Local;
import model.TypeArticle;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleController {

    public boolean ajouterArticle(Article a){
        String sql = "INSERT INTO Article(reference, codeBarres, designation, description, type, datePeremption, stockMinimal, estCritique,idlocal) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pst.setString(1, a.getReference());
            pst.setString(2, a.getCodeBarres());
            pst.setString(3, a.getDesignation());
            pst.setString(4, a.getDescription());
            pst.setString(5, a.getType().name());
            if(a.getDatePeremption() != null)
                pst.setDate(6, new java.sql.Date(a.getDatePeremption().getTime()));
            else
                pst.setNull(6, Types.DATE);
            pst.setInt(7, a.getStockMinimal());
            pst.setBoolean(8, a.isEstCritique());
            pst.setInt(9, a.getIdlocal());

            int affected = pst.executeUpdate();
            if(affected > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()) a.setIdArticle(rs.getInt(1));
                return true;
            }
            return false;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifierArticle(Article a){
        String sql = "UPDATE Article SET reference=?, codeBarres=?, designation=?, description=?, type=?, datePeremption=?, stockMinimal=?, estCritique=?, idlocal=? WHERE idArticle=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, a.getReference());
            pst.setString(2, a.getCodeBarres());
            pst.setString(3, a.getDesignation());
            pst.setString(4, a.getDescription());
            pst.setString(5, a.getType().name());

            if (a.getDatePeremption() != null)
                pst.setDate(6, new java.sql.Date(a.getDatePeremption().getTime()));
            else
                pst.setNull(6, Types.DATE);

            pst.setInt(7, a.getStockMinimal());
            pst.setBoolean(8, a.isEstCritique());

            pst.setInt(9, a.getIdlocal());

            pst.setInt(10, a.getIdArticle());

            return pst.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerArticle(int id){
        String sql = "DELETE FROM Article WHERE idArticle=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Article> getAllArticles(){
        List<Article> list = new ArrayList<>();
        String sql = "SELECT * FROM Article";
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()) list.add(map(rs));

        } catch(Exception e){ e.printStackTrace(); }
        return list;
    }

    public List<Article> rechercherArticle(
            String motCle,
            Date datePeremption,
            TypeArticle type,
            Integer idLocal) {

        List<Article> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Article WHERE 1=1");

        if (motCle != null && !motCle.isEmpty()) {
            sql.append(" AND (reference ILIKE ? OR codeBarres ILIKE ? OR designation ILIKE ?)");
        }
        if (datePeremption != null) {
            sql.append(" AND datePeremption = ?");
        }
        if (type != null) {
            sql.append(" AND type = ?");
        }
        if (idLocal != null) {
            sql.append(" AND idLocal = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (motCle != null && !motCle.isEmpty()) {
                pst.setString(index++, "%" + motCle + "%");
                pst.setString(index++, "%" + motCle + "%");
                pst.setString(index++, "%" + motCle + "%");
            }
            if (datePeremption != null) {
                pst.setDate(index++, datePeremption);
            }
            if (type != null) {
                pst.setString(index++, type.name());
            }
            if (idLocal != null) {
                pst.setInt(index, idLocal);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Article> getArticlesByMouvement(String typeMvt, Date dateDebut, Date dateFin) {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* " +
                "FROM Article a " +
                "JOIN Mouvement m ON a.idArticle = m.idArticle " +
                "WHERE m.idLocal = a.idLocal ";

        if (typeMvt != null && !typeMvt.isEmpty()) {
            sql += " AND m.typeMouvement = ?";
        }
        if (dateDebut != null) {
            sql += " AND m.dateMouvement >= ?";
        }
        if (dateFin != null) {
            sql += " AND m.dateMouvement <= ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            int idx = 1;
            if (typeMvt != null && !typeMvt.isEmpty()) {
                pst.setString(idx++, typeMvt);
            }
            if (dateDebut != null) {
                pst.setTimestamp(idx++, new java.sql.Timestamp(dateDebut.getTime()));
            }
            if (dateFin != null) {
                pst.setTimestamp(idx++, new java.sql.Timestamp(dateFin.getTime() + 24*60*60*1000));
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Article a = new Article();
                a.setIdArticle(rs.getInt("idArticle"));
                a.setReference(rs.getString("reference"));
                a.setDesignation(rs.getString("designation"));
                a.setDescription(rs.getString("description"));
                a.setCodeBarres(rs.getString("codeBarres"));
                a.setStockMinimal(rs.getInt("stockMinimal"));
                a.setEstCritique(rs.getBoolean("estCritique"));
                if (rs.getDate("datePeremption") != null)
                    a.setDatePeremption(rs.getDate("datePeremption"));

                String typeStr = rs.getString("type");
                if (typeStr != null && !typeStr.isEmpty()) {
                    a.setType(TypeArticle.valueOf(typeStr));
                }

                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }








    public List<Article> getArticlesStockAlerte() {
        List<Article> list = new ArrayList<>();

        String sql = """
        SELECT 
            a.idArticle,
            a.reference,
            a.designation,
            a.stockMinimal,
            a.type,
            a.datePeremption,
            a.codebarres,
            a.description,
            a.idLocal,
            l.nomLocal,
            COALESCE(s.quantiteActuelle, 0) AS stockLocal
        FROM Article a
        LEFT JOIN Stock s ON a.idArticle = s.idArticle AND a.idLocal = s.idLocal
        LEFT JOIN Local l ON a.idLocal = l.idLocal
        WHERE COALESCE(s.quantiteActuelle,0) < a.stockMinimal
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Article a = new Article();
                a.setIdArticle(rs.getInt("idArticle"));
                a.setReference(rs.getString("designation"));
                a.setCodeBarres(rs.getString("codeBarres"));
                a.setDesignation(rs.getString("reference"));
                a.setStockMinimal(rs.getInt("stockMinimal"));
                a.setType(TypeArticle.valueOf(rs.getString("type"))); // conversion String → enum
                a.setDatePeremption(rs.getDate("datePeremption"));
                a.setDescription(rs.getString("description"));
                a.setIdlocal(rs.getInt("idLocal"));
                a.setNomLocal(rs.getString("nomLocal"));

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Article> getArticlesConsommablesCritiques() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT * FROM Article WHERE estCritique = TRUE AND type LIKE 'CONSOMMABLE%'";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()) {
                Article a = new Article();
                a.setIdArticle(rs.getInt("idArticle"));
                a.setReference(rs.getString("reference"));
                a.setDesignation(rs.getString("designation"));
                a.setDescription(rs.getString("description"));
                a.setCodeBarres(rs.getString("codeBarres"));
                a.setStockMinimal(rs.getInt("stockMinimal"));
                a.setType(TypeArticle.valueOf(rs.getString("type")));
                a.setDatePeremption(rs.getDate("datePeremption"));
                a.setEstCritique(rs.getBoolean("estCritique"));
                list.add(a);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }




    public Article getArticleById(int id) {
        Article article = null;
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM article WHERE idarticle = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                article = new Article();
                article.setIdArticle(rs.getInt("idarticle"));
                article.setReference(rs.getString("nom"));
                article.setDescription(rs.getString("description"));

            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return article;
    }



    public List<Article> getArticlesAlerte(){
        List<Article> list = new ArrayList<>();
        String sql = "SELECT * FROM Article WHERE datePeremption IS NOT NULL AND datePeremption <= CURRENT_DATE + INTERVAL '7 days'";
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()) list.add(map(rs));

        } catch(Exception e){ e.printStackTrace(); }
        return list;
    }
    private Article map(ResultSet rs) throws SQLException {
        Article a = new Article();
        a.setIdArticle(rs.getInt("idArticle"));
        a.setReference(rs.getString("reference"));
        a.setDesignation(rs.getString("designation"));
        a.setCodeBarres(rs.getString("codeBarres"));
        a.setDescription(rs.getString("description"));
        if(rs.getString("type") != null)
            a.setType(TypeArticle.valueOf(rs.getString("type")));
        a.setDatePeremption(rs.getDate("datePeremption"));
        a.setStockMinimal(rs.getInt("stockMinimal"));
        a.setEstCritique(rs.getBoolean("estCritique"));
        try {
            a.setIdlocal(rs.getInt("idlocal"));
        } catch(SQLException e) {
            a.setIdlocal(null);
        }
        return a;
    }

    public List<Local> getAllLocaux() {
        List<Local> list = new ArrayList<>();
        String sql = "SELECT * FROM Local";
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            while(rs.next()) {
                Local l = new Local();
                l.setIdLocal(rs.getInt("idLocal"));
                l.setNomLocal(rs.getString("nomLocal"));
                // Ajoutez d'autres attributs si nécessaire
                list.add(l);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}