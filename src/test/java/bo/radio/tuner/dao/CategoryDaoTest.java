package bo.radio.tuner.dao;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import bo.radio.tuner.dao.utils.FileUtils;
import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.Station;

public class CategoryDaoTest {
	private static final String TRASHFOLDER_PATH = "src/test/resources/db";
	private static final String DB_URL = "jdbc:h2:src/test/resources/db/tuner";
	
	private CategoryDao dao;
	private Category testCategory;
	private Station testStation1;
	private Station testStation2;

	@Before
	public void setUp() throws SQLException {
		TunerDaoUtils.createAllTables(DB_URL);

		dao = new CategoryDao(DB_URL);

		testCategory = new Category("Test-Category");

		testStation1 = new Station("Radio 1", "http://test");
		testStation1.getCategories().add(testCategory);

		testStation2 = new Station("Radio 2", "http://test2");
		testStation2.getCategories().add(testCategory);

		testCategory.getStations().add(testStation1);
		testCategory.getStations().add(testStation2);

		testCategory = dao.create(testCategory);
	}

	@Test
	public void testCreateExistingCategory() throws SQLException {
		dao.create(testCategory);

		// Assertions
		List<Category> categories = dao.getAll();
		assertThat(categories.size(), is(1));
		assertThat(categories.get(0), is(equalTo(testCategory)));
	}

	@Test
	public void testRead() throws SQLException {
		int catId = testCategory.getId();
		Category fetchedCat = dao.getById(catId);

		// Assert
		assertThat(fetchedCat, is(equalTo(testCategory)));
		assertThat(fetchedCat.getStations(), containsInAnyOrder(testStation1, testStation2));
	}

	@Test
	public void testUpdate() throws SQLException {
		String newCatName = "Rock100%";
		testCategory.setName(newCatName);

		Station aStation = new Station("American Rock Radio", "http://test:8080");
		aStation.getCategories().add(testCategory);
		testCategory.getStations().add(aStation);

		dao.update(testCategory);

		// Assert
		Category fetchCat = dao.getById(testCategory.getId());
		assertThat(fetchCat.getName(), is(equalTo(newCatName)));
		assertThat(fetchCat.getStations(), containsInAnyOrder(testStation1, testStation2, aStation));
	}

	@Test
	public void testDelete() throws SQLException {
		dao.delete(testCategory);

		// Assert
		List<Category> categories = dao.getAll();
		assertThat(categories, is(empty()));
	}
	
	@Test
	public void testGetAllCategories() throws SQLException {
		for(Category c : dao.getAll()) {
			assertThat(c, is(equalTo(testCategory)));
			assertThat(c.getStations(), containsInAnyOrder(testStation1, testStation2));
		}
	}

	@Test
	public void testGetAllStations() throws SQLException {
		List<Station> stations = dao.queryMany(testCategory);
		// Assert
		assertThat(stations, containsInAnyOrder(testStation1, testStation2));
	}
	
	@Test
	public void testGetStationsByName() throws SQLException {
		List<Category> categories = dao.getEntitiesByColumn(Station.NAMECOLUMN_NAME, testCategory.getName());
		// Assert
		assertThat(categories.size(), is(1));
		assertThat(categories.get(0), is(equalTo(testCategory)));
		assertThat(categories.get(0).getStations(), containsInAnyOrder(testStation1, testStation2));
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
