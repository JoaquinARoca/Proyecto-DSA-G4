package dsa.proyecto.G4.models;

public class Product {
    private String id;
    private String nombre;
    private Integer precio;

    // Constructor con parámetros
    public Product(String id, String nombre, Integer precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Constructor vacío
    public Product() {}

    // Getters y Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    @Override
    public String toString(){return "Product [id="+id+"name="+nombre+"price="+precio+"]";}
}
