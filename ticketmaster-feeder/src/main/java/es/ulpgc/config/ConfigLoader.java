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
                throw new IllegalStateException("Configuration file not found.");
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading configuration file: " + ex.getMessage());
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) return null;
        value = value.trim();
        if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (OutputStream output = new java.io.FileOutputStream("ticketmaster-feeder/src/main/resources/config.properties")) {
            properties.store(output, null);
        } catch (IOException exception) {
            System.err.println("Error saving configuration file: " + exception.getMessage());
        }
    }
}
