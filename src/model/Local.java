package model;

import java.util.List;

public class Local {
    private int idLocal;
    private String nomLocal;
    private TypeLocal typeLocal;
    private int capacite;
    private String adresse;

    private List<Stock> stocks;

    public Local() {}


    public int getIdLocal() { return idLocal; }
    public void setIdLocal(int idLocal) { this.idLocal = idLocal; }

    public String getNomLocal() { return nomLocal; }
    public void setNomLocal(String nomLocal) { this.nomLocal = nomLocal; }

    public TypeLocal getTypeLocal() {
        return typeLocal;
    }

    public void setTypeLocal(TypeLocal typeLocal) {
        this.typeLocal = typeLocal;
    }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public List<Stock> getStocks() { return stocks; }
    public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

    @Override
    public String toString() {
        return nomLocal;
    }
}