package model;

import java.sql.Timestamp;

public class Controle2 {

    private Integer idControle;
    private Timestamp dateControle;
    private int quantite;
    private EtatArticle etat;
    private String observation;
    private int idArticle;
    private int idLocalSource;
    private int idLocalDestination;
    private int idCommandeInterne;

    public Controle2() {}

    public Controle2(Timestamp dateControle, int quantite, EtatArticle etat, String observation,
                     int idArticle, int idLocalSource, int idLocalDestination, int idCommandeInterne) {
        this.dateControle = dateControle;
        this.quantite = quantite;
        this.etat = etat;
        this.observation = observation;
        this.idArticle = idArticle;
        this.idLocalSource = idLocalSource;
        this.idLocalDestination = idLocalDestination;
        this.idCommandeInterne = idCommandeInterne;
    }


    public Integer getIdControle() { return idControle; }
    public void setIdControle(Integer idControle) { this.idControle = idControle; }

    public Timestamp getDateControle() { return dateControle; }
    public void setDateControle(Timestamp dateControle) { this.dateControle = dateControle; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public EtatArticle getEtat() { return etat; }
    public void setEtat(EtatArticle etat) { this.etat = etat; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public int getIdArticle() { return idArticle; }
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }

    public int getIdLocalSource() { return idLocalSource; }
    public void setIdLocalSource(int idLocalSource) { this.idLocalSource = idLocalSource; }

    public int getIdLocalDestination() { return idLocalDestination; }
    public void setIdLocalDestination(int idLocalDestination) { this.idLocalDestination = idLocalDestination; }

    public int getIdCommandeInterne() { return idCommandeInterne; }
    public void setIdCommandeInterne(int idCommandeInterne) { this.idCommandeInterne = idCommandeInterne; }
}