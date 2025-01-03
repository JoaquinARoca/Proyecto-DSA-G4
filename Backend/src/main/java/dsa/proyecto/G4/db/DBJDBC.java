package dsa.proyecto.G4.db;

import java.sql.*;

public class DBJDBC {
    public static void insert() throws SQLException{

        Connection connection = DBUtils.getConnection();
        Statement statement1  = connection.createStatement();
        statement1.execute("INSERT INTO User (id, nombre, contraseña) VALUES (4, 'prueba1','123')");
        // i = x / 0
        connection.close();

    }
    private static String getType (int type) {
        String ret = null;
        switch (type) {
            case Types.VARCHAR:
                ret ="VARCHAR";
                break;
            case Types.INTEGER:
                ret = "INTEGER";
                break;
            case Types.DATE:
                ret = "DATE";
                break;
        }

        return ret;
    }


    public static void findAll() throws Exception {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id, nombre, contraseña FROM User WHERE 1=1");

            // INTROSPECCIÓN DE LA BBDD
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("Número de columnas: " + rsmd.getColumnCount());
            int i = 1;
            while (i <= rsmd.getColumnCount()) {
                System.out.println("Columna " + i + " Nombre: " + rsmd.getColumnName(i) +
                        " Tipo: " + rsmd.getColumnType(i) +
                        " " + DBJDBC.getType(rsmd.getColumnType(i)));
                i++;
            }

            int id;
            String x, nombre, contraseña;

            // Iterar sobre los resultados
            while (rs.next()) {
                id = rs.getInt("id");
                nombre = rs.getString("nombre");
                contraseña = rs.getString("contraseña");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Contraseña: " + contraseña);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        findAll();
    }

}
