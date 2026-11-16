package model;

public class LigneInventaire {

    private int idLigne;
    private int idInventaire;
    private int idArticle;
    private int idLocal;
    private int quantite;


    private Article article;
    private Local local;

    public LigneInventaire() {}

    public int getIdLigne() { return idLigne; }
    public void setIdLigne(int idLigne) { this.idLigne = idLigne; }

    public int getIdInventaire() { return idInventaire; }
    public void setIdInventaire(int idInventaire) { this.idInventaire = idInventaire; }

    public int getIdArticle() { return idArticle; }
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }

    public int getIdLocal() { return idLocal; }
    public void setIdLocal(int idLocal) { this.idLocal = idLocal; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    // --- Getters et setters pour les objets ---
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }
}