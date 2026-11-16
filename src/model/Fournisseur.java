package model;

public class Fournisseur {
    private int idFournisseur;
    private String nomFournisseur;
    private String adresse;
    private String telephone;
    private String email;
    private String contactPrincipal;

    public Fournisseur() {}

    public Fournisseur(String nom, String adresse, String tel, String email, String contact) {
        this.nomFournisseur = nom;
        this.adresse = adresse;
        this.telephone = tel;
        this.email = email;
        this.contactPrincipal = contact;
    }

    public int getIdFournisseur() { return idFournisseur; }
    public void setIdFournisseur(int id) { this.idFournisseur = id; }

    public String getNomFournisseur() { return nomFournisseur; }
    public void setNomFournisseur(String nomFournisseur) { this.nomFournisseur = nomFournisseur; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactPrincipal() { return contactPrincipal; }
    public void setContactPrincipal(String contactPrincipal) { this.contactPrincipal = contactPrincipal; }
    @Override
    public String toString() {
        return nomFournisseur;
    }
}