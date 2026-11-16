package model;

public class Trace {
    private int idTrace;
    private String action;
    private String dateAction;
    private String utilisateur;

    public int getIdTrace() { return idTrace; }
    public void setIdTrace(int idTrace) { this.idTrace = idTrace; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDateAction() { return dateAction; }
    public void setDateAction(String dateAction) { this.dateAction = dateAction; }
    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }
}