package model;

public class Service {

    private int idService;
    private String nomService;
    private String typeService;
    private String responsable; // facultatif ou autre info utile

    public Service() {}

    public Service(String nomService, String typeService, String responsable){
        this.nomService = nomService;
        this.typeService = typeService;
        this.responsable = responsable;
    }

    public int getIdService() { return idService; }
    public void setIdService(int idService) { this.idService = idService; }

    public String getNomService() { return nomService; }
    public void setNomService(String nomService) { this.nomService = nomService; }

    public String getTypeService() { return typeService; }
    public void setTypeService(String typeService) { this.typeService = typeService; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    @Override
    public String toString() {
        return nomService;
    }
}