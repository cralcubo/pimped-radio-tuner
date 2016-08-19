package bo.radio.tuner.business;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public class StationBusinessTest extends TunerBusinessTest {
	
	@Test
	public void testSaveStation() throws TunerPersistenceException {
		Station station = new Station("Dance Radio", "http://test:8080");
		Category category = new Category("General");
		
		category.getStations().add(station);
		station.getCategories().add(category);
		
		Station savedStation = stationController.saveStation(station);
		Category savedCategory = savedStation.getCategories().get(0);
		
		// Assert
		Station fetchedStation = stationController.getStationById(savedStation.getId()).get();
		Category fetchedCategory = catController.getCategory(savedCategory.getId()).get();
		
		assertThat(fetchedStation, is(equalTo(savedStation)));
		assertThat(fetchedCategory, is(equalTo(savedCategory)));
	}
	
	@Test
	public void testDeleteStation() throws TunerPersistenceException {
		Category category = new Category("General");
		category = catController.createCategory(category);
		
		Station station = new Station("Dance Radio", "http://test:8080");
		station.getCategories().add(category);
		station = stationController.saveStation(station);
		
		// Delete Station
		Station fStation = stationController.getStationById(station.getId()).get();
		stationController.removeStation(fStation);
		
		// Assertions
		assertThat(stationController.getAllStations().isEmpty(), is(true));
		Category fCat = catController.getCategory(category.getId()).get();
		assertThat(fCat.getStations().isEmpty(), is(true));
	}
	
	@Test
	public void testUpdateStation() throws TunerPersistenceException {
		Category category = new Category("General");
		category = catController.createCategory(category);
		
		Station station = new Station("Dance Radio", "http://test:8080");
		station.getCategories().add(category);
		station = stationController.saveStation(station);
		
		// Create a new Category
		Category newCat = new Category("New Cat");
		newCat = catController.createCategory(newCat);
		
		// Add new category to saved station
		Station fStation = stationController.getStationById(station.getId()).get();
		fStation.getCategories().add(newCat);
		
		stationController.updateStation(fStation);
		
		// Assert
		fStation = stationController.getStationById(station.getId()).get();
		assertThat(fStation.getCategories(), containsInAnyOrder(newCat, category));
	}
	
	@Test
	public void testSaveRepeatedStreamUrlStation() throws TunerPersistenceException {
		Category category = new Category("General");
		category = catController.createCategory(category);
		
		Station s1 = new Station("testStation1", "http://test");
		s1.getCategories().add(category);
		s1 = stationController.saveStation(s1);
		
		Station s2 = new Station("testStation2", "http://test");
		s2.getCategories().add(category);
		s2 = stationController.saveStation(s2);
		
		Station s3 = new Station("testStation3", "http://test");
		s3.getCategories().add(category);
		s3 = stationController.saveStation(s3);
		
		// Assertions
		assertThat(stationController.getAllStations().size(), is(1));
		assertThat(stationController.getAllStations().get(0), is(equalTo(s1)));
		assertThat(stationController.getAllStations().get(0), is(equalTo(s2)));
		assertThat(stationController.getAllStations().get(0), is(equalTo(s3)));
		
		Category fCat = catController.getCategory(category.getId()).get();
		assertThat(fCat.getStations().size(), is(1));
		assertThat(fCat.getStations().get(0), is(equalTo(s1)));
		assertThat(fCat.getStations().get(0), is(equalTo(s2)));
		assertThat(fCat.getStations().get(0), is(equalTo(s3)));
	}
	
	@Test
	public void testGetStationsByCategory() throws TunerPersistenceException {
		Station s1 = new Station("Radio Paradise", "http://test");
		Station s2 = new Station("Radio Tropical", "http://test2");
		Station s3 = new Station("Radio Nirvana", "http://test3");
		Station s4 = new Station("Radio Rock", "http://test4");
		
		Category rockCat = new Category("Rock");
		Category favCat = new Category("My Favs");
		Category generalCat = new Category("General");
		
		generalCat.getStations().addAll(Arrays.asList(s1, s2, s3, s4));
		s1.getCategories().add(generalCat);
		s2.getCategories().add(generalCat);
		s3.getCategories().add(generalCat);
		s4.getCategories().add(generalCat);
		
		// Save all categories
		s1 = stationController.saveStation(s1);
		s2 = stationController.saveStation(s2);
		s3 = stationController.saveStation(s3);
		s4 = stationController.saveStation(s4);
		
		// Create all categories
		rockCat = catController.createCategory(rockCat);
		favCat = catController.createCategory(favCat);
		generalCat = catController.createCategory(generalCat);
		
		// Assert
		
		assertThat(catController.getAllCategories().size(), is(3));
		assertThat(catController.getAllCategories(), containsInAnyOrder(rockCat, favCat, generalCat));
		
		assertThat(stationController.getAllStations().size(), is(4));
		assertThat(stationController.getAllStations(), containsInAnyOrder(s1, s2, s3, s4));
		
		//get all stations from general
		Category fGenCat = catController.getCategory(generalCat.getId()).get();
		assertThat(fGenCat.getStations(), containsInAnyOrder(s1, s2, s3, s4));
		
		//add stations to rock cat
		Category fRockCat = catController.getCategory(rockCat.getId()).get();
		fRockCat.getStations().addAll(Arrays.asList(s1, s4));
		catController.updateCategory(fRockCat);
		
		assertThat(fRockCat.getStations(), containsInAnyOrder(s1, s4));
		assertThat(catController.getCategory(rockCat.getId()).get().getStations(), containsInAnyOrder(s1, s4));
		assertThat(stationController.getStationById(s1.getId()).get().getCategories(), containsInAnyOrder(fRockCat, fGenCat));
		
		// add stations to favCat
		Category fFavCat = catController.getCategory(favCat.getId()).get();
		fFavCat.getStations().add(s1);
		catController.updateCategory(fFavCat);
		
		assertThat(catController.getCategory(fFavCat.getId()).get().getStations().size(), is(1));
		assertThat(catController.getCategory(fFavCat.getId()).get().getStations().get(0), is(equalTo(s1)));
		
		assertThat(stationController.getStationById(s1.getId()).get().getCategories(), containsInAnyOrder(fRockCat, fGenCat, fFavCat));
	}
	
	@Test
	public void testRemoveStationsInCategory() throws TunerPersistenceException {
		/* 01. Create the general category */
		Category generalCat = new Category("General");
		generalCat = catController.createCategory(generalCat);
		
		/* 02. Save the new stations */
		Station s1 = new Station("Radio Paradise", "http://test");
		Station s2 = new Station("Radio Tropical", "http://test2");
		Station s3 = new Station("Radio Nirvana", "http://test3");
		Station s4 = new Station("Radio Rock", "http://test4");
		
		s1.getCategories().add(generalCat);
		s2.getCategories().add(generalCat);
		s3.getCategories().add(generalCat);
		s4.getCategories().add(generalCat);
		
		s1 = stationController.saveStation(s1);
		s2 = stationController.saveStation(s2);
		s3 = stationController.saveStation(s3);
		s4 = stationController.saveStation(s4);
		
		/* 03. Assert that general cat has all the stations  */
		Category fGenCat = catController.getCategory(generalCat.getId()).get();
		assertThat(fGenCat.getStations().size(), is(4));
		assertThat(fGenCat.getStations(), containsInAnyOrder(s1, s2, s3, s4));
		
		/* 04. Remove some stations */
		fGenCat = catController.getCategory(generalCat.getId()).get();
		fGenCat.getStations().removeAll(Arrays.asList(s2, s3));
		catController.updateCategory(fGenCat);
		
		/* 05. Assert that we have the expected stations */
		fGenCat = catController.getCategory(generalCat.getId()).get();
		assertThat(fGenCat.getStations().size(), is(2));
		assertThat(fGenCat.getStations(), containsInAnyOrder(s1, s4));
		
	}
	
	@Test
	public void testAddStationsInCategory() throws TunerPersistenceException {
		/* 01. Create the general category */
		Category generalCat = new Category("General");
		generalCat = catController.createCategory(generalCat);
		
		/* 02. Save the new stations */
		Station s1 = new Station("Radio Paradise", "http://test");
		Station s2 = new Station("Radio Tropical", "http://test2");
		Station s3 = new Station("Radio Nirvana", "http://test3");
		Station s4 = new Station("Radio Rock", "http://test4");
		
		s1.getCategories().add(generalCat);
		s2.getCategories().add(generalCat);
		s3.getCategories().add(generalCat);
		s4.getCategories().add(generalCat);
		
		s1 = stationController.saveStation(s1);
		s2 = stationController.saveStation(s2);
		s3 = stationController.saveStation(s3);
		s4 = stationController.saveStation(s4);
		
		/* 03. Create a new Category and add some stations */
		Category rockCat = new Category("Rock");
		rockCat.getStations().addAll(Arrays.asList(s1, s4));
		rockCat = catController.createCategory(rockCat);
		
		/* 04. Assert that the new Cat has the expected stations */
		Category fRockCat = catController.getCategory(rockCat.getId()).get();
		assertThat(fRockCat.getStations().size(), is(2));
		assertThat(fRockCat.getStations(), containsInAnyOrder(s1, s4));
		
		/* 05. Assert that the stations contains the expected categories */
		Station fS1 = stationController.getStationById(s1.getId()).get();
		Station fS2 = stationController.getStationById(s2.getId()).get();
		Station fS3 = stationController.getStationById(s3.getId()).get();
		Station fS4 = stationController.getStationById(s4.getId()).get();
		
		Category fGenCat = catController.getCategory(generalCat.getId()).get();
		
		assertThat(fS1.getCategories(), containsInAnyOrder(fGenCat, fRockCat));
		assertThat(fS2.getCategories(), containsInAnyOrder(fGenCat));
		assertThat(fS3.getCategories(), containsInAnyOrder(fGenCat));
		assertThat(fS4.getCategories(), containsInAnyOrder(fGenCat, fRockCat));
	}
	
	@Test
	public void testGetStationByStationObject() throws TunerPersistenceException {
		/*
		 * 01. Add stations with a general category
		 */
		Category generalCat = new Category("General");
		generalCat = catController.createCategory(generalCat);
		
		Station s1 = new Station("Radio Paradise", "http://test");
		Station s2 = new Station("Radio Tropical", "http://test2");
		Station s3 = new Station("Radio Nirvana", "http://test3");
		Station s4 = new Station("Radio Rock", "http://test4");
		
		s1.getCategories().add(generalCat);
		s2.getCategories().add(generalCat);
		s3.getCategories().add(generalCat);
		s4.getCategories().add(generalCat);
		
		stationController.saveStation(s1);
		stationController.saveStation(s2);
		stationController.saveStation(s3);
		stationController.saveStation(s4);
		
		/*
		 * 02. Get the stations saved by Stream URL
		 */
		
		for(Station s : Arrays.asList(s1, s2, s3, s4)) {
			Optional<Station> os = stationController.findStation(s);
			assertThat(os.isPresent(), is(true));
			assertThat(os.get(), is(s));
		}
	}
	
	@Test
	public void getUnexistentStation() throws TunerPersistenceException {
		Station s1 = new Station("Radio Paradise", "http://test");
		Optional<Station> os = stationController.findStation(s1);
		
		// Assert
		assertThat(os.isPresent(), is(false));
	}
	
}
