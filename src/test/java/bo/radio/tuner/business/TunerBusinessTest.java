package bo.radio.tuner.business;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import bo.radio.tuner.CategoryDaoApi;
import bo.radio.tuner.StationDaoApi;
import bo.radio.tuner.TunerDaoApi;
import bo.radio.tuner.dao.TunerDaoUtils;
import bo.radio.tuner.dao.utils.FileUtils;

public class TunerBusinessTest {
	private final static String RESOURCES_PATH = "src/test/resources";
	private final static String DATABASEPROP_PATH = RESOURCES_PATH + "/database.properties";
	private final static String TRASHFOLDER_PATH = 	RESOURCES_PATH + "/db";
	private String dbUrl;

	protected CategoryDaoApi catController;
	protected StationDaoApi stationController;

	@Before
	public void setUp() throws Exception {
		Properties databaseProperties = new Properties();
		try (FileInputStream fis = new FileInputStream(DATABASEPROP_PATH)) {
			databaseProperties.load(fis);
		}
		dbUrl = databaseProperties.getProperty("database.url");
		TunerDaoApi tc = new TunerBusiness(databaseProperties);
		tc.init();

		catController = tc.getCategoryControllerInstance();
		stationController = tc.getStationControllerInstance();
	}

	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(dbUrl);
	}

	@AfterClass
	public static void cleanUp() {
		// Remove all the files created inside the resources folder
		FileUtils.removeFolderContents(new File(TRASHFOLDER_PATH));
	}

}
