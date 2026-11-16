package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Inventaire {

    private int idInventaire;
    private Date dateInventaire;
    private StatutInventaire statut;
    private String notes;
    private List<LigneInventaire> lignes = new ArrayList<>();

    public Inventaire() {
        this.statut = StatutInventaire.EN_COURS; }


    public StatutInventaire getStatut() {
        return statut;
    }

    public void setStatut(StatutInventaire statut) {
        this.statut = statut;
    }


    public List<LigneInventaire> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneInventaire> lignes) {
        this.lignes = lignes;
    }

    public void ajouterLigne(LigneInventaire ligne) {
        this.lignes.add(ligne);
    }

    public int getIdInventaire() {
        return idInventaire;
    }

    public void setIdInventaire(int idInventaire) {
        this.idInventaire = idInventaire;
    }

    public Date getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(Date dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}