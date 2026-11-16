package model;

import java.util.ArrayList;
import java.util.List;

public class CommandeExterne {

    private int idCommande;
    private int idFournisseur;
    private StatutCommande statut;
    private double montant;


    private List<LigneCommande> lignesCommande = new ArrayList<>();

    public CommandeExterne() {}


    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

    public int getIdFournisseur() { return idFournisseur; }
    public void setIdFournisseur(int idFournisseur) { this.idFournisseur = idFournisseur; }

    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public List<LigneCommande> getLignesCommande() { return lignesCommande; }
    public void setLignesCommande(List<LigneCommande> lignesCommande) { this.lignesCommande = lignesCommande; }


    public void ajouterLigne(LigneCommande ligne) { this.lignesCommande.add(ligne); }
}