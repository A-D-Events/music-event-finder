package es.ulpgc.config;

import java.io.IOException;
import java.io.InputStream;
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
}
