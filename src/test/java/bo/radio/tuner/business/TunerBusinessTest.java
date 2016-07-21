package bo.radio.tuner.business;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import bo.radio.tuner.CategoryController;
import bo.radio.tuner.StationController;
import bo.radio.tuner.TunerController;
import bo.radio.tuner.dao.TunerDaoUtils;

public class TunerBusinessTest {
	private final static String DATABASEPROP_PATH = "src/test/resources/database.properties";
	private String dbUrl;
	
	protected CategoryController catController;
	protected StationController stationController;
	
	
	@Before
	public void setUp() throws Exception {
		Properties databaseProperties = new Properties();
		try(FileInputStream fis = new FileInputStream(DATABASEPROP_PATH)){
			databaseProperties.load(fis);
		}
		dbUrl = databaseProperties.getProperty("database.url");
		TunerController tc = new TunerBusiness(databaseProperties);
		tc.init();
		
		catController = tc.getCategoryControllerInstance();
		stationController = tc.getStationControllerInstance();
	}
	
	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(dbUrl);
	}

}
