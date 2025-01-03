package dsa.proyecto.G4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBJDBC2 extends DBJDBC {
    public static void insert() throws SQLException {
        Connection connection = DBUtils.getConnection();

        // Consulta SQL actualizada para reflejar solo los campos necesarios
        String theQuery = "INSERT INTO User (id, nombre, contraseña) VALUES (?, ?, ?)";

        // Usar PreparedStatement para evitar SQL injection
        PreparedStatement statement = connection.prepareStatement(theQuery);

        // Valores para los campos
        statement.setInt(1, 0); // ID
        statement.setString(2, "UsuarioEjemplo"); // Nombre
        statement.setString(3, "ContraseñaSegura123"); // Contraseña

        // Ejecutar la consulta
        statement.execute();

        // Cerrar la conexión
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        insert();
        findAll();
    }

}
