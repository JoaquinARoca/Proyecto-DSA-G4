package dsa.proyecto.G4.db.orm.dao;

import dsa.proyecto.G4.models.Purchase;

import java.util.List;

public interface IPurchaseDAO {
    void addPurchase(String idU, String idP, int cantidad);

    List<Purchase> getCompras(String id);
}
