package dsa.proyecto.G4.db.orm;

import dsa.proyecto.G4.db.orm.util.ObjectHelper;

import java.sql.*;
import java.util.*;

public class SessionImpl implements Session {

    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Object entity) {
        String[] fields = ObjectHelper.getFields(entity);
        String columns = String.join(", ", fields);
        String placeholders = String.join(", ", Collections.nCopies(fields.length, "?"));
        String query = "INSERT INTO " + entity.getClass().getSimpleName().toLowerCase() +
                " (" + columns + ") VALUES (" + placeholders + ")";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1; // Comenzar desde el índice 1
            for (String field : fields) {
                Object value = ObjectHelper.getter(entity, field);
                stmt.setObject(i++, value); // Asignar los valores dinámicamente
            }
            // Ejecutar la consulta
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(Class<?> theClass, String pk, Object value) {
        String query = "SELECT * FROM " + theClass.getSimpleName().toLowerCase() + " WHERE " + pk + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Object instance = theClass.getDeclaredConstructor().newInstance();
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    ObjectHelper.setter(instance, meta.getColumnName(i), rs.getObject(i));
                }
                return instance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Object> findAll(Class<?> theClass) {
        List<Object> list = new LinkedList<>();
        String query = "SELECT * FROM " + theClass.getSimpleName().toLowerCase();
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Object instance = theClass.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    ObjectHelper.setter(instance, meta.getColumnName(i), rs.getObject(i));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object> findAll(Class<?> theClass, HashMap<String, String> params) throws SQLException {
        List<Object> list = new LinkedList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(theClass.getSimpleName().toLowerCase());

        if (params != null && !params.isEmpty()) {
            query.append(" WHERE ");
            for (String key : params.keySet()) {
                query.append(key).append(" = ? AND ");
            }
            query.delete(query.length() - 5, query.length()); // Eliminar el último " AND "
        }

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int index = 1;
            for (String value : params.values()) {
                stmt.setString(index++, value);
            }
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Object instance = theClass.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    ObjectHelper.setter(instance, meta.getColumnName(i), rs.getObject(i));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error executing findAll with params", e);
        }
        return list;
    }

    @Override
    public List<Object> getList(Class<?> theClass, String key, Object value) {
        List<Object> list = new LinkedList<>();
        String query = "SELECT * FROM " + theClass.getSimpleName().toLowerCase() + " WHERE " + key + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Object instance = theClass.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    ObjectHelper.setter(instance, meta.getColumnName(i), rs.getObject(i));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object> query(String query, Class<?> theClass, HashMap<String, Object> params) {
        List<Object> list = new LinkedList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1;
            for (Object value : params.values()) {
                stmt.setObject(i++, value);
            }

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Object instance = theClass.getDeclaredConstructor().newInstance();
                for (int j = 1; j <= meta.getColumnCount(); j++) {
                    ObjectHelper.setter(instance, meta.getColumnName(j), rs.getObject(j));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Object object) throws SQLException {
        // Obtener los campos y valores del objeto
        String[] fields = ObjectHelper.getFields(object);
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(object.getClass().getSimpleName().toLowerCase()).append(" SET ");

        // Construir la parte del SET de la consulta SQL
        for (String field : fields) {
            if (!field.equalsIgnoreCase("id")) { // Suponemos que "id" es la clave primaria y no se actualiza
                query.append(field).append(" = ?, ");
            }
        }
        query.delete(query.length() - 2, query.length()); // Eliminar la última coma y espacio

        // Añadir la cláusula WHERE para la clave primaria
        query.append(" WHERE id = ?");

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int index = 1;

            // Asignar valores para los campos dinámicamente
            for (String field : fields) {
                if (!field.equalsIgnoreCase("id")) {
                    stmt.setObject(index++, ObjectHelper.getter(object, field));
                }
            }

            // Asignar el valor del ID para la cláusula WHERE
            stmt.setObject(index, ObjectHelper.getter(object, "id"));

            // Ejecutar la consulta
            stmt.executeUpdate();
            System.out.println("Update query executed successfully: " + query);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating the object: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Object object) throws SQLException {
        String query = "DELETE FROM " + object.getClass().getSimpleName().toLowerCase() + " WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, ObjectHelper.getter(object, "ID"));
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
