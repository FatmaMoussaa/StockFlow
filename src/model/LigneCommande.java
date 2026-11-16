package model;

public class LigneCommande {

    private int idLigne;
    private int idCommande;
    private int idArticle;
    private int idLocal;
    private int quantite;


    public LigneCommande() { }

    public LigneCommande(int idCommande, int idArticle, int idLocal, int quantite) {
        this.idCommande = idCommande;
        this.idArticle = idArticle;
        this.idLocal = idLocal;
        this.quantite = quantite;
    }


    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }


    @Override
    public String toString() {
        return "Article ID: " + idArticle + ", Local ID: " + idLocal + ", Qté: " + quantite;
    }
}