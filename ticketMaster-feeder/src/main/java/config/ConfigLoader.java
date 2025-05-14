package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	private static final Properties properties = new Properties();

	static {
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Missing config.properties");
			}
			properties.load(input);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
