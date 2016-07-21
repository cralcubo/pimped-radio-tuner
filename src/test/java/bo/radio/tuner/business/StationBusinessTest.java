package bo.radio.tuner.business;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

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
		Station fetchedStation = stationController.getStation(savedStation.getId());
		Category fetchedCategory = catController.getCategory(savedCategory.getId());
		
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
		Station fStation = stationController.getStation(station.getId());
		stationController.removeStation(fStation);
		
		// Assertions
		assertThat(stationController.getAllStations().isEmpty(), is(true));
		Category fCat = catController.getCategory(category.getId());
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
		Station fStation = stationController.getStation(station.getId());
		fStation.getCategories().add(newCat);
		
		stationController.updateStation(fStation);
		
		// Assert
		fStation = stationController.getStation(station.getId());
		assertThat(fStation.getCategories(), containsInAnyOrder(newCat, category));
	}
	
	@Test
	public void testSaveRepeatedNameStation() throws TunerPersistenceException {
		Category category = new Category("General");
		category = catController.createCategory(category);
		
		Station s1 = new Station("testStation", "http://test");
		s1.getCategories().add(category);
		s1 = stationController.saveStation(s1);
		
		Station s2 = new Station("testStation", "http://test");
		s2.getCategories().add(category);
		s2 = stationController.saveStation(s2);
		
		// Assertions
		assertThat(stationController.getAllStations().size(), is(1));
		assertThat(stationController.getAllStations().get(0), is(equalTo(s1)));
		assertThat(stationController.getAllStations().get(0), is(equalTo(s2)));
		
		Category fCat = catController.getCategory(category.getId());
		assertThat(fCat.getStations().size(), is(1));
		assertThat(fCat.getStations().get(0), is(equalTo(s1)));
		assertThat(fCat.getStations().get(0), is(equalTo(s2)));
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
		Category fGenCat = catController.getCategory(generalCat.getId());
		assertThat(fGenCat.getStations(), containsInAnyOrder(s1, s2, s3, s4));
		
		//add stations to rock cat
		Category fRockCat = catController.getCategory(rockCat.getId());
		fRockCat.getStations().addAll(Arrays.asList(s1, s4));
		catController.updateCategory(fRockCat);
		
		assertThat(fRockCat.getStations(), containsInAnyOrder(s1, s4));
		assertThat(catController.getCategory(rockCat.getId()).getStations(), containsInAnyOrder(s1, s4));
		assertThat(stationController.getStation(s1.getId()).getCategories(), containsInAnyOrder(fRockCat, fGenCat));
		
		// add stations to favCat
		Category fFavCat = catController.getCategory(favCat.getId());
		fFavCat.getStations().add(s1);
		catController.updateCategory(fFavCat);
		
		assertThat(catController.getCategory(fFavCat.getId()).getStations().size(), is(1));
		assertThat(catController.getCategory(fFavCat.getId()).getStations().get(0), is(equalTo(s1)));
		
		assertThat(stationController.getStation(s1.getId()).getCategories(), containsInAnyOrder(fRockCat, fGenCat, fFavCat));
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
		Category fGenCat = catController.getCategory(generalCat.getId());
		assertThat(fGenCat.getStations().size(), is(4));
		assertThat(fGenCat.getStations(), containsInAnyOrder(s1, s2, s3, s4));
		
		/* 04. Remove some stations */
		fGenCat = catController.getCategory(generalCat.getId());
		fGenCat.getStations().removeAll(Arrays.asList(s2, s3));
		catController.updateCategory(fGenCat);
		
		/* 05. Assert that we have the expected stations */
		fGenCat = catController.getCategory(generalCat.getId());
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
		Category fRockCat = catController.getCategory(rockCat.getId());
		assertThat(fRockCat.getStations().size(), is(2));
		assertThat(fRockCat.getStations(), containsInAnyOrder(s1, s4));
		
		/* 05. Assert that the stations contains the expected categories */
		Station fS1 = stationController.getStation(s1.getId());
		Station fS2 = stationController.getStation(s2.getId());
		Station fS3 = stationController.getStation(s3.getId());
		Station fS4 = stationController.getStation(s4.getId());
		
		Category fGenCat = catController.getCategory(generalCat.getId());
		
		assertThat(fS1.getCategories(), containsInAnyOrder(fGenCat, fRockCat));
		assertThat(fS2.getCategories(), containsInAnyOrder(fGenCat));
		assertThat(fS3.getCategories(), containsInAnyOrder(fGenCat));
		assertThat(fS4.getCategories(), containsInAnyOrder(fGenCat, fRockCat));
	}
}
