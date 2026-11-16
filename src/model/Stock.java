package model;

import java.util.Date;

public class Stock {

    private int idStock;
    private Article article;   // Référence à l'article
    private Local local;       // Référence au local
    private int quantiteActuelle;
    private int quantiteReservee;
    private Date dateMAJ;

    public Stock() {}

    public Stock(Article article, Local local, int quantiteActuelle, int quantiteReservee, Date dateMAJ) {
        this.article = article;
        this.local = local;
        this.quantiteActuelle = quantiteActuelle;
        this.quantiteReservee = quantiteReservee;
        this.dateMAJ = dateMAJ;
    }

    // Getters et Setters
    public int getIdStock() { return idStock; }
    public void setIdStock(int idStock) { this.idStock = idStock; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }

    public int getQuantiteActuelle() { return quantiteActuelle; }
    public void setQuantiteActuelle(int quantiteActuelle) { this.quantiteActuelle = quantiteActuelle; }

    public int getQuantiteReservee() { return quantiteReservee; }
    public void setQuantiteReservee(int quantiteReservee) { this.quantiteReservee = quantiteReservee; }

    public Date getDateMAJ() { return dateMAJ; }
    public void setDateMAJ(Date dateMAJ) { this.dateMAJ = dateMAJ; }

    @Override
    public String toString() {
        return article.getReference() + " - " + local.getNomLocal();
    }
}