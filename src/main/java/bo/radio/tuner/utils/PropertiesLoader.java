package bo.radio.tuner.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertiesLoader {
	private final static Logger log = LoggerFactory.getLogger(PropertiesLoader.class);
	
	private static final String PROPFILE_PATH = "src/main/resources/database.properties";
	private static final Properties p = new Properties();
	static {
		try {
			p.load(new FileInputStream(PROPFILE_PATH));
		} catch (FileNotFoundException e) {
			log.error("Database info property file not found.", e);
		} catch (IOException e) {
			log.error("Error loading database property file", e);
		}
	}

	public static String getProperty(String name) {
		return p.getProperty(name);
	}

}
