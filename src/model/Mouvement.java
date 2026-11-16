package model;

import java.sql.Timestamp;

public class Mouvement {

    private Integer idMouvement;
    private Integer idArticle;
    private Integer idLocal;
    private TypeMouvement typeMouvement;
    private Integer quantite;
    private Timestamp dateMouvement;
    private String observation;
    private Integer idCommandeInterne;
    private Integer idCommandeExterne;


    public Mouvement() {}

    public Mouvement(Integer idArticle, Integer idLocal, TypeMouvement typeMouvement,
                     Integer quantite, Timestamp dateMouvement, String observation,
                     Integer idCommandeInterne, Integer idCommandeExterne) {
        this.idArticle = idArticle;
        this.idLocal = idLocal;
        this.typeMouvement = typeMouvement;
        this.quantite = quantite;
        this.dateMouvement = dateMouvement;
        this.observation = observation;
        this.idCommandeInterne = idCommandeInterne;
        this.idCommandeExterne = idCommandeExterne;

    }
    private String nomArticle;

    public String getNomArticle() { return nomArticle; }
    public void setNomArticle(String nomArticle) { this.nomArticle = nomArticle; }
    public Integer getIdMouvement() { return idMouvement; }
    public void setIdMouvement(Integer idMouvement) { this.idMouvement = idMouvement; }

    public Integer getIdArticle() { return idArticle; }
    public void setIdArticle(Integer idArticle) { this.idArticle = idArticle; }

    public Integer getIdLocal() { return idLocal; }
    public void setIdLocal(Integer idLocal) { this.idLocal = idLocal; }

    public TypeMouvement getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(TypeMouvement typeMouvement) { this.typeMouvement = typeMouvement; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Timestamp getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(Timestamp dateMouvement) { this.dateMouvement = dateMouvement; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public Integer getIdCommandeInterne() { return idCommandeInterne; }
    public void setIdCommandeInterne(Integer idCommandeInterne) { this.idCommandeInterne = idCommandeInterne; }

    public Integer getIdCommandeExterne() { return idCommandeExterne; }
    public void setIdCommandeExterne(Integer idCommandeExterne) { this.idCommandeExterne = idCommandeExterne; }

}