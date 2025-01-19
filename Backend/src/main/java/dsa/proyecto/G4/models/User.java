package dsa.proyecto.G4.models;

import dsa.proyecto.G4.util.RandomUtils;

public class User {
    private String id;
    private String nombre;
    private String contraseña;
    private Integer saldo;
    private String perfil;

    public User () {
        this.setId(RandomUtils.getId());
    }

    public User(String nombre, String contraseña) {
        this(null,nombre,contraseña,"1",0);
    }

    public User(String id, String nombre, String contraseña) {
        this();
        if(id!=null) this.setId(id);
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.saldo = 0;
        this.perfil = "1";
    }
    public User(String id, String nombre, String contraseña, Integer saldo) {
        this();
        if(id!=null) this.setId(id);
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.saldo = saldo;
        this.perfil = "1";
    }
    public User(String id, String nombre, String contraseña, String perfil, Integer saldo) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.perfil = perfil;
        this.saldo = saldo;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public Integer getSaldo() {
        return saldo;
    }
    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getPerfil() {
        return perfil;
    }
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}

