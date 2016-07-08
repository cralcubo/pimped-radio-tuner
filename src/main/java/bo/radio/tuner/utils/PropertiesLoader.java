package bo.radio.tuner.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private final String PROPFILE_PATH = "src/main/resources/database.properties";
	private static final Properties p = new Properties();
	{
		try {
			p.load(new FileInputStream(PROPFILE_PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getProperty(String name) {
		return p.getProperty(name);
	}

}
