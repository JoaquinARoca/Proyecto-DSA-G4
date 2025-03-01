package dsa.proyecto.G4.db.orm.dao;

import dsa.proyecto.G4.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    // Crear un usuario
    void addUser(String id, String nombre, String contraseña, int saldo, String perfil);

    // Obtener un usuario por su ID
    User getUser(String id);

    // Obtener un usuario por su nombre
    User getUserByName(String nombre);

    // Eliminar un usuario por su ID
    void deleteUser(String id) throws SQLException;

    // Actualizar la información de un usuario
    void updateUser(String id, String nombre, String contraseña, Integer saldo, String perfil) throws SQLException;

    // Obtener la lista de todos los usuarios
    List<User> getUsers();
}
