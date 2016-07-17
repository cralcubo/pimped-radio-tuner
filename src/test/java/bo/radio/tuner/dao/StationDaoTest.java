package bo.radio.tuner.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
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
		testCategory1.getStations().add(testStation);

		testCategory2 = new Category("My Favorits");
		testCategory2.getStations().add(testStation);

		testStation.getCategories().add(testCategory1);
		testStation.getCategories().add(testCategory2);

		testStation = dao.create(testStation);
	}

	@Test
	public void testReadStation() throws SQLException {
		Station fetchedStation = dao.getById(testStation.getId());

		assertThat(testStation, is(equalTo(fetchedStation)));
		assertThat(fetchedStation.getCategories(), containsInAnyOrder(testCategory1, testCategory2));
	}

	@Test
	public void testReadAllStations() throws SQLException {
		Station station = new Station("anotherStation", "http://another_stream.com");

		station.getCategories().add(testCategory1);
		// Save the new Station in DB
		dao.create(station);

		// Get all stations
		List<Station> allStations = dao.getAll();

		// Assert
		assertThat(allStations.size(), is(2));
		assertThat(allStations, containsInAnyOrder(station, testStation));
		for (Station s : allStations) {
			if (s.getCategories().size() == 1) {
				assertThat(s, is(station));
				assertThat(s.getCategories(), contains(testCategory1));
			} else if (s.getCategories().size() == 2) {
				assertThat(s, is(testStation));
				assertThat(s.getCategories(), contains(testCategory1, testCategory2));
			} else {
				Assert.fail("Station " + s + " unexpected.");
			}
		}

	}

	@Test
	public void testDeleteStation() throws SQLException {
		Station station = new Station("anotherStation", "http://another_stream.com");

		station.getCategories().add(testCategory1);

		Station stationCreated = dao.create(station);

		// Assert that there are 2 stations
		List<Station> stations = dao.getAll();
		assertThat("Number of stations unexpected.", stations.size(), is(2));

		// Delete the testStation
		dao.delete(testStation);

		// Assert that there is just 1 station
		List<Station> stationsAfter = dao.getAll();
		assertThat(stationsAfter.size(), is(1));
		assertThat("Unexpected station", stationsAfter.get(0), is(equalTo(stationCreated)));

		// Assert that no categories are attached to the deleted station
		List<Category> categories = dao.queryMany(testStation);
		assertThat("No categories expected.", categories.isEmpty(), is(true));
	}

	@Test
	public void testUpdateStation() throws SQLException {
		String newName = "new test name";
		testStation.setName(newName);

		dao.update(testStation);

		// Assert
		Station updatedStation = dao.getById(testStation.getId());

		assertThat(testStation, is(equalTo(updatedStation)));
	}

	@Test
	public void testUpdateStation_addCategories() throws SQLException {
		Category nc = new Category("Classics");
		nc.getStations().add(testStation);
		testStation.getCategories().add(nc);
		// Update the DB
		dao.update(testStation);

		// Fetch the station from DB and assert
		Station fetchedStation = dao.getById(testStation.getId());
		assertThat(fetchedStation.getCategories().size(), is(3));
		assertThat(fetchedStation.getCategories(), containsInAnyOrder(nc, testCategory1, testCategory2));
	}

	@Test
	public void testUpdateStation_removeCategories() throws SQLException {
		testStation.getCategories().remove(testCategory1);
		// Update
		dao.update(testStation);
		// Fetch the station from DB and assert
		Station fetchedStation = dao.getById(testStation.getId());
		assertThat(fetchedStation.getCategories().size(), is(1));
		assertThat(fetchedStation.getCategories(), containsInAnyOrder(testCategory2));

	}

	@Test
	public void testUpdateStation_addRemoveCategories() throws SQLException {
		Category nc = new Category("Classics");
		nc.getStations().add(testStation);
		testStation.getCategories().add(nc);
		testStation.getCategories().remove(testCategory1);

		// Update the DB
		dao.update(testStation);

		// Fetch the station from DB and assert
		Station fetchedStation = dao.getById(testStation.getId());
		assertThat(fetchedStation.getCategories().size(), is(2));
		assertThat(fetchedStation.getCategories(), containsInAnyOrder(nc, testCategory2));

	}

	@After
	public void tearDown() throws SQLException {
		TunerDaoUtils.deleteAllTables(DB_URL);
		dao.close();
	}

}
