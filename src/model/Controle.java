package model;

import java.sql.Timestamp;

public class Controle {

    private Integer idControle;
    private Timestamp dateControle;
    private Integer quantiteControllee;
    private EtatArticle etat;
    private String observation;
    private Integer idArticle;
    private Integer idLocalSource;
    private Integer idLocalDestination;
    private Integer idCommandeInterne;
    private Integer idCommandeExterne;


    public Controle() {}

    public Controle(Timestamp dateControle, Integer quantiteControllee, EtatArticle etat, String observation,
                    Integer idArticle, Integer idLocalSource, Integer idLocalDestination,
                    Integer idCommandeInterne, Integer idCommandeExterne) {
        this.dateControle = dateControle;
        this.quantiteControllee = quantiteControllee;
        this.etat = etat;
        this.observation = observation;
        this.idArticle = idArticle;
        this.idLocalSource = idLocalSource;
        this.idLocalDestination = idLocalDestination;
        this.idCommandeInterne = idCommandeInterne;
        this.idCommandeExterne = idCommandeExterne;
    }


    public Integer getIdControle() { return idControle; }
    public void setIdControle(Integer idControle) { this.idControle = idControle; }

    public Timestamp getDateControle() { return dateControle; }
    public void setDateControle(Timestamp dateControle) { this.dateControle = dateControle; }

    public Integer getQuantiteControllee() { return quantiteControllee; }
    public void setQuantiteControllee(Integer quantiteControllee) { this.quantiteControllee = quantiteControllee; }

    public EtatArticle getEtat() { return etat; }
    public void setEtat(EtatArticle etat) { this.etat = etat; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public Integer getIdArticle() { return idArticle; }
    public void setIdArticle(Integer idArticle) { this.idArticle = idArticle; }

    public Integer getIdLocalSource() { return idLocalSource; }
    public void setIdLocalSource(Integer idLocalSource) { this.idLocalSource = idLocalSource; }

    public Integer getIdLocalDestination() { return idLocalDestination; }
    public void setIdLocalDestination(Integer idLocalDestination) { this.idLocalDestination = idLocalDestination; }

    public Integer getIdCommandeInterne() { return idCommandeInterne; }
    public void setIdCommandeInterne(Integer idCommandeInterne) { this.idCommandeInterne = idCommandeInterne; }

    public Integer getIdCommandeExterne() { return idCommandeExterne; }
    public void setIdCommandeExterne(Integer idCommandeExterne) { this.idCommandeExterne = idCommandeExterne; }

}