package model;

import java.sql.Timestamp;

public class Utilisateur {
    private int idUtilisateur;
    private String username;
    private String password;
    private String role;
    private String nomComplet;
    private String email;
    private Timestamp dateCreation;
    private Timestamp dernierLogin;
    private boolean actif;


    public Utilisateur() {}

    public Utilisateur(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }

    public Timestamp getDernierLogin() { return dernierLogin; }
    public void setDernierLogin(Timestamp dernierLogin) { this.dernierLogin = dernierLogin; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}