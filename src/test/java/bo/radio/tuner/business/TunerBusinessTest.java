package bo.radio.tuner.business;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import bo.radio.tuner.CategoryDaoController;
import bo.radio.tuner.StationDaoController;
import bo.radio.tuner.TunerDaoController;
import bo.radio.tuner.dao.TunerDaoUtils;

public class TunerBusinessTest {
	private final static String DATABASEPROP_PATH = "src/test/resources/database.properties";
	private String dbUrl;
	
	protected CategoryDaoController catController;
	protected StationDaoController stationController;
	
	
	@Before
	public void setUp() throws Exception {
		Properties databaseProperties = new Properties();
		try(FileInputStream fis = new FileInputStream(DATABASEPROP_PATH)){
			databaseProperties.load(fis);
		}
		dbUrl = databaseProperties.getProperty("database.url");
		TunerDaoController tc = new TunerBusiness(databaseProperties);
		tc.init();
		
		catController = tc.getCategoryControllerInstance();
		stationController = tc.getStationControllerInstance();
	}
	
	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(dbUrl);
	}

}
