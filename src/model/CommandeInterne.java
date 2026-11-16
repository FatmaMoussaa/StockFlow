package model;

import java.util.ArrayList;
import java.util.List;

public class CommandeInterne {

    private int idCommande;
    private int idService;
    private StatutCommande statut;
    private List<LigneCommandeInterne> lignesCommande;

    public CommandeInterne() {
        lignesCommande = new ArrayList<>();
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public List<LigneCommandeInterne> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommandeInterne> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }


    public void ajouterLigne(LigneCommandeInterne ligne) {
        lignesCommande.add(ligne);
    }

    public void supprimerLigne(LigneCommandeInterne ligne) {
        lignesCommande.remove(ligne);
    }

    @Override
    public String toString() {
        return "CommandeInterne{" +
                "idCommande=" + idCommande +
                ", idService=" + idService +
                ", statut=" + statut +
                ", lignesCommande=" + lignesCommande.size() +
                '}';
    }
}