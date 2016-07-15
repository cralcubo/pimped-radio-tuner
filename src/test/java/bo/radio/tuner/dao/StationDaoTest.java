package bo.radio.tuner.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.Station;

public class StationDaoTest {
	private static final String DB_URL = "jdbc:h2:src/test/resources:tuner";
	
	private StationDao dao;
	private Station testStation;
	private Category testCategory1;
	private Category testCategory2;
	
	@Before
	public void setUp() throws SQLException {
		TunerDaoUtils.createAllTables(DB_URL);
		dao = new StationDao(DB_URL);
		testStation = new Station("Test Radio", "http://test.com");
		testCategory1 = new Category("Rock");
		testCategory2 = new Category("My Favorits");

		testStation = dao.create(testStation, Arrays.asList(testCategory1, testCategory2));
	}
	
	@Test
	public void testSaveStation() throws SQLException {
		Station fetchedStation = dao.getById(testStation.getId());
		
		assertThat(testStation, is(equalTo(fetchedStation)));
	}
	
	@Test
	public void testGetCategories() throws SQLException {
		// Assert
		List<Category> categoriesDB = dao.getMany(testStation);
		assertThat(categoriesDB.isEmpty(), is(false));
		assertThat(categoriesDB.size(), is(2));

		for (Category c : new ArrayList<>(categoriesDB)) {
			if (c.getName().equals(testCategory1.getName()) || c.getName().equals(testCategory2.getName())) {
				categoriesDB.remove(c);
			}
		}

		assertThat(categoriesDB.isEmpty(), is(true));
	}
	
	@Test
	public void testDeleteSatation() throws SQLException {
		Station station = new Station("anotherStation", "http://another_stream.com");
		Station stationCreated = dao.create(station, Arrays.asList(testCategory1));
		
		// Assert that there are 2 stations
		List<Station> stations = dao.getAllOnes();
		assertThat("Number of stations unexpected.", stations.size(), is(2));
		
		// Delete the testStation
		dao.delete(testStation);
		
		// Assert that there is just 1 station
		List<Station> stationsAfter = dao.getAllOnes();
		assertThat(stationsAfter.size(), is(1));
		assertThat("Unexpected station", stationsAfter.get(0), is(equalTo(stationCreated)));
		
		// Assert that no categories are attached to the deleted station
		List<Category> categories  = dao.getMany(testStation);
		assertThat("No categories expected.", categories.isEmpty(), is(true));
	}
	
	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(DB_URL);
		dao.close();
	}

}
