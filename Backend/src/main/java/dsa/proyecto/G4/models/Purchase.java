package dsa.proyecto.G4.models;

public class Purchase {
    private Integer idPurchase;
    private String idU;
    private String idP;
    private int cantidad;

    public Purchase() {
    }

    public Purchase(String idU, String idP, Integer cantidad) {
        this.idU = idU;
        this.idP = idP;
        this.cantidad = cantidad;
    }

    public Purchase(Integer id, String idU, Integer cantidad, String idP) {
        this.idPurchase = id;
        this.idU = idU;
        this.cantidad = cantidad;
        this.idP = idP;
    }

    public Integer getIdPurchase() {
        return idPurchase;
    }
    public void setIdPurchase(Integer idPurchase) {
        this.idPurchase = idPurchase;
    }

    public String getIdU() {
        return idU;
    }
    public void setIdU(String idU) {
        this.idU = idU;
    }

    public String getIdP() {
        return idP;
    }
    public void setIdP(String idP) {
        this.idP = idP;
    }

    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
