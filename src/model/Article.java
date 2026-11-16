package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Date;
import java.util.List;

public class Article {

    private int idArticle;
    private String codeBarres;
    private TypeArticle type;
    private Date datePeremption;
    private int stockMinimal;
    private boolean estCritique;
    private StringProperty reference;
    private StringProperty designation;
    private String description;
    private Integer idlocal;
    private String nomLocal;




    public String getNomLocal() {
        return nomLocal;
    }

    public void setNomLocal(String nomLocal) {
        this.nomLocal = nomLocal;
    }

    public void setReference(StringProperty reference) {
        this.reference = reference;
    }

    public void setDesignation(StringProperty designation) {
        this.designation = designation;
    }

    public Integer getIdlocal() {
        return idlocal;
    }

    public void setIdlocal(Integer idlocal) {
        this.idlocal = idlocal;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Mouvement> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<Mouvement> mouvements) {
        this.mouvements = mouvements;
    }


    private List<Stock> stocks;
    private List<Mouvement> mouvements;

    public Article() {
        this.reference = new SimpleStringProperty("");
        this.designation = new SimpleStringProperty("");
    }

    public Article(String reference, String designation) {
        this.reference = new SimpleStringProperty(reference);
        this.designation = new SimpleStringProperty(designation);
    }


    public String getReference() { return reference.get(); }
    public void setReference(String value) { reference.set(value); }
    public StringProperty referenceProperty() { return reference; }

    public String getDesignation() { return designation.get(); }
    public void setDesignation(String value) { designation.set(value); }
    public StringProperty designationProperty() { return designation; }


    public int getIdArticle() { return idArticle; }
    public void setIdArticle(int id) { this.idArticle = id; }

    public String getCodeBarres() { return codeBarres; }
    public void setCodeBarres(String codeBarres) { this.codeBarres = codeBarres; }

    public TypeArticle getType() { return type; }
    public void setType(TypeArticle type) { this.type = type; }

    public Date getDatePeremption() { return datePeremption; }
    public void setDatePeremption(Date datePeremption) { this.datePeremption = datePeremption; }

    public int getStockMinimal() { return stockMinimal; }
    public void setStockMinimal(int stockMinimal) { this.stockMinimal = stockMinimal; }

    public boolean isEstCritique() { return estCritique; }
    public void setEstCritique(boolean estCritique) { this.estCritique = estCritique; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return reference.get();
    }
}