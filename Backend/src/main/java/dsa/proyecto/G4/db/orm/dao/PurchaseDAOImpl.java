package dsa.proyecto.G4.db.orm.dao;

import dsa.proyecto.G4.db.orm.FactorySession;
import dsa.proyecto.G4.db.orm.Session;
import dsa.proyecto.G4.models.Purchase;
import dsa.proyecto.G4.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PurchaseDAOImpl implements IPurchaseDAO {
    private static PurchaseDAOImpl instance;
    final static Logger logger = Logger.getLogger(PurchaseDAOImpl.class.getName());
    public static PurchaseDAOImpl getInstance(){
        if(instance==null)
            instance=new PurchaseDAOImpl();
        return instance;
    }

    @Override
    public void addPurchase(String idU, String idP, int cantidad){
        Session session = null;
        try {
            session = FactorySession.openSession();
            Purchase purchase = new Purchase(idU,idP,cantidad);
            session.save(purchase);
        } catch (Exception e) {
            logger.severe("Error adding user: " + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Purchase> getCompras(String id){
        Session session = null;
        List<Purchase> purchases = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            HashMap<String, String> params = new HashMap<>();
            params.put("idu", id); // Asegúrate de que el nombre del campo coincide con la base de datos
            List<Object> results = session.findAll(Purchase.class, params);
            for (Object obj : results) {
                if (obj instanceof Purchase) {
                    purchases.add((Purchase) obj);
                }
            }
        } catch (Exception e) {
            logger.severe("Error retrieving purchases for user " + id + ": " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return purchases;
    }
}
