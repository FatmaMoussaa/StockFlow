package model;

public class Statistique {
    private String nomArticle;
    private String typeArticle;
    private int quantiteTotale;
    private int nbServices;
    private String service;

    public Statistique(String nomArticle, String typeArticle, int quantiteTotale,
                       int nbServices, String service) {
        this.nomArticle = nomArticle;
        this.typeArticle = typeArticle;
        this.quantiteTotale = quantiteTotale;
        this.nbServices = nbServices;
        this.service = service;
    }


    public String getNomArticle() { return nomArticle; }
    public String getTypeArticle() { return typeArticle; }
    public int getQuantiteTotale() { return quantiteTotale; }
    public int getNbServices() { return nbServices; }
    public String getService() { return service; }
}