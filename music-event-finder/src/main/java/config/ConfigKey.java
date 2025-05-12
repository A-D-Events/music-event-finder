package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigKey {
	private static final Properties properties = new Properties();

	static {
		try (InputStream input = ConfigKey.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Could not find config.properties file.");
			}
			properties.load(input);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}
	}

	public static String getApiKey() {
		return getProperty("api.key");
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
