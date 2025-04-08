package es.ulpgc.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el archivo de configuración.");
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de configuración: " + ex.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (OutputStream output = new java.io.FileOutputStream("src/main/resources/config.properties")) {
            properties.store(output, null);
        } catch (IOException ex) {
            System.err.println("Error al guardar el archivo de configuración: " + ex.getMessage());
        }
    }
}
