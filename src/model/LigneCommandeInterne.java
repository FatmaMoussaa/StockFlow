package model;

public class LigneCommandeInterne {

    private int idLigne;
    private int idCommande;
    private int idArticle;
    private int idLocalSource;
    private int idLocalDestination;
    private int quantite;
    private boolean retourEffectue;

    public boolean isRetourEffectue() {
        return retourEffectue;
    }

    public void setRetourEffectue(boolean retourEffectue) {
        this.retourEffectue = retourEffectue;
    }

    public int getIdLigne() { return idLigne; }
    public void setIdLigne(int idLigne) { this.idLigne = idLigne; }

    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

    public int getIdArticle() { return idArticle; }
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }

    public int getIdLocalSource() { return idLocalSource; }
    public void setIdLocalSource(int idLocalSource) { this.idLocalSource = idLocalSource; }

    public int getIdLocalDestination() { return idLocalDestination; }
    public void setIdLocalDestination(int idLocalDestination) { this.idLocalDestination = idLocalDestination; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }


}