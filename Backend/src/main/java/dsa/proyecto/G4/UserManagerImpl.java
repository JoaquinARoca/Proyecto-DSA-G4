package dsa.proyecto.G4;

import dsa.proyecto.G4.models.*;

import java.util.*;
import java.util.logging.Logger;

public class UserManagerImpl implements UserManager {
    private static UserManagerImpl instance;
    private List<User> usuarios;
    final static Logger logger = Logger.getLogger(String.valueOf(UserManagerImpl.class));

    private UserManagerImpl() {
        this.usuarios = new LinkedList<>();
    }

    public static UserManagerImpl getInstance() {
        if (instance == null) {
            instance = new UserManagerImpl();
        }
        return instance;
    }

    @Override
    public User getUsuarioPorId(String id) {
        for (User usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public User getUsuarioPorNombre(String nombre) {
        for (User usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public User addUsuario(User u) {
        usuarios.add(u);
        return u;
    }

    @Override
    public void addUsuarios(List<User> users){
        this.usuarios = users;
    }

    public User addUsuario(String id,String nombre,String contraseña){
        return this.addUsuario(new User(id, nombre,contraseña));
    }
    public User addUsuario(String nombre, String contraseña){
        return this.addUsuario(null,nombre,contraseña);
    }

    @Override
    public List<User> getUsuarios() {
        return this.usuarios;
    }

    @Override
    public User updateUser(String id,User u){
        User u1 = this.getUsuarioPorId(id);
        if(u!=null){
           removeUsuario(id);
            u1.setNombre(u.getNombre());
            u1.setContraseña(u.getContraseña());
            u1.setSaldo(u.getSaldo());
            u1.setPerfil(u.getPerfil());
            addUsuario(u1);
        }else {
            logger.warning("not found"+u);
        }
        return u1;
    }

    @Override
    public boolean removeUsuario(String id) {
        return usuarios.removeIf(usuario -> usuario.getId().equals(id));
    }

    @Override
    public int countUsers(){//cambios 4.7
        return usuarios.size();
    }

    @Override
    public User buscaUsuario(User user){//cambios 4.7
        for(User usuario : usuarios){
            if(usuario.getNombre().equals(user.getNombre())  && usuario.getContraseña().equals(user.getContraseña()))
                return  null;
        }

        return user;
    }

    @Override
    public List<Purchase> ordenaInventario(List<Purchase> purchases){
        // Mapa para agrupar por idP
        Map<String, Purchase> groupedPurchases = new HashMap<>();

        for (Purchase purchase : purchases) {
            // Si ya existe un registro para este idP, sumamos la cantidad
            if (groupedPurchases.containsKey(purchase.getIdP())) {
                Purchase existingPurchase = groupedPurchases.get(purchase.getIdP());
                existingPurchase.setCantidad(existingPurchase.getCantidad() + purchase.getCantidad());
            } else {
                // Si no existe, añadimos una nueva entrada al mapa
                groupedPurchases.put(purchase.getIdP(), new Purchase(purchase.getIdU(), purchase.getIdP(), purchase.getCantidad()));
            }
        }

        // Convertir los valores del mapa de nuevo en una lista
        return new ArrayList<>(groupedPurchases.values());
    }
    @Override
    public Integer calculaNuevoSaldo(String id, List<Purchase> purchases, List<Product> products){
        Product comprado = new Product();
        int saldoActual = getUsuarioPorId(id).getSaldo();
        for(Purchase purchase: purchases)
            for(Product p : products) {
                if (p.getId().equals(purchase.getIdP()))
                    comprado = p;
                if(comprado!=null) {
                    int precio = purchase.getCantidad() * comprado.getPrecio();
                    saldoActual = saldoActual - precio;
                    comprado=null;
                }
            }
        return saldoActual;
    }
}
