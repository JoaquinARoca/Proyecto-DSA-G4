package dsa.proyecto.G4.db.orm.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ObjectHelper {
    public static String[] getFields(Object entity) {
        Class<?> theClass = entity.getClass();
        Field[] fields = theClass.getDeclaredFields();
        String[] sFields = new String[fields.length];

        int i = 0;
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                sFields[i++] = field.getName();
            }
        }

        return Arrays.copyOf(sFields, i);
    }

    public static void setter(Object object, String property, Object value) {
        String propToUppercase = property.substring(0, 1).toUpperCase() + property.substring(1);
        String setterName = "set" + propToUppercase;
        try {
            Method m = object.getClass().getMethod(setterName, value.getClass());
            m.invoke(object, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static String getMethodName(String property) {
        return property.substring(0,1).toUpperCase()+property.substring(1);
    }

    public static Object getter(Object object, String property) {
        String propToUppercase = property.substring(0, 1).toUpperCase() + property.substring(1);
        String getterName = "get" + propToUppercase;
        String booleanGetterName = "is" + propToUppercase;

        try {
            Method m;
            try {
                // Intentar con el getter estándar
                m = object.getClass().getMethod(getterName);
            } catch (NoSuchMethodException e) {
                // Intentar con el getter booleano
                m = object.getClass().getMethod(booleanGetterName);
            }

            // Hacer accesible si no es público
            m.setAccessible(true);
            return m.invoke(object);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No se encontró el método: " + getterName + " o " + booleanGetterName, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error al invocar el método: " + getterName, e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Acceso denegado al método: " + getterName, e);
        }
    }

}