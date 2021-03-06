package bo.radio.tuner.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import bo.radio.tuner.dao.utils.FileUtils;

public class TunerDaoUtilsTest {
	private static final String TRASHFOLDER_PATH = "src/test/resources/db";
	private static final String DB_URL = "jdbc:h2:src/test/resources/db/tuner";
	
	
	@Test
	public void testCreateAllTables() throws SQLException {
		TunerDaoUtils.createAllTables(DB_URL);
		
		// Assertions
		Connection conn = DriverManager.getConnection(DB_URL);
		
		DatabaseMetaData dbm = conn.getMetaData();
		ResultSet rs = dbm.getTables(null, null, "%", new String[]{"TABLE"});
		
		List<String> existingTables = new ArrayList<>();
		while(rs.next()) {
			existingTables.add(rs.getString(3).toLowerCase());
		}
		
		List<String> expectedTables = TunerDaoUtils.getAllTableNames();
		MatcherAssert.assertThat(expectedTables, Matchers.containsInAnyOrder(existingTables.toArray()));
	}
	
	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(DB_URL);
	}
	
	@AfterClass
	public static void cleanUp() {
		FileUtils.removeFolderContents(new File(TRASHFOLDER_PATH));
	}

}
