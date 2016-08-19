package bo.radio.tuner.business;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public class CategoryStationTest extends TunerBusinessTest {
	
	@Test
	public void testGetCategoryByName() throws TunerPersistenceException {
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		
		Station s1 = new Station("testStation1", "http://test1");
		s1.getCategories().add(cat);
		s1 = stationController.saveStation(s1);
		
		Station s2 = new Station("testStation2", "http://test2");
		s2.getCategories().add(cat);
		s2 = stationController.saveStation(s2);
		
		cat.getStations().add(s1);
		cat.getStations().add(s2);
		
		// Assertions
		
		Optional<Category> oCat = catController.findCategoryByName(cat.getName());
		assertThat(oCat.isPresent(), is(true));
		Category savedCat = oCat.get();
		assertThat(savedCat, is(equalTo(cat)));
		assertThat(savedCat.getStations(), containsInAnyOrder(s1, s2));
	}
	
	@Test
	public void testCreateCategory() throws TunerPersistenceException{
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		
		// Assert
		Category fCat = catController.getCategory(cat.getId()).get();
		
		assertThat(fCat, is(equalTo(cat)));
	}
	
	@Test
	public void testDeleteCategory() throws TunerPersistenceException{
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		
		// Assert
		catController.deleteCategory(cat);
		
		assertThat(catController.getAllCategories(), is(empty()));
	}
	
	@Test
	public void testUpdateCategory() throws TunerPersistenceException {
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		String newName = "newName";
		cat.setName(newName);
		
		catController.updateCategory(cat);
		
		// Assert
		Category fCat = catController.getCategory(cat.getId()).get();
		assertThat(fCat.getName(), is(equalTo(newName)));
	}
	
	@Test
	public void testChangeCatNameAndSaveNewCatSameName() throws TunerPersistenceException {
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		String newName = "newName";
		cat.setName(newName);
		
		catController.updateCategory(cat);
		
		Category cat2 = new Category("test");
		cat2 = catController.createCategory(cat2);
		
		// Assert
		assertThat(catController.getAllCategories().size(), is(2));
		assertThat(catController.getAllCategories(), contains(cat, cat2));
	}
	
	@Test
	public void testCreateCategory_repeatedName() throws TunerPersistenceException {
		Station s = new Station("test", "http://test");
		
		Category cat = new Category("Rock 100%");
		cat.getStations().add(s);
		s.getCategories().add(cat);
		cat = catController.createCategory(cat);
		
		Category cat2 = new Category("Rock 100%");
		cat2.getStations().add(s);
		s.getCategories().add(cat2);
		cat2 = catController.createCategory(cat2);
		
		Category cat3 = new Category("Rock 100%");
		cat3.getStations().add(s);
		s.getCategories().add(cat3);
		cat3 = catController.createCategory(cat3);
		
		// Assert
		List<Category> allCategories = catController.getAllCategories(); 
		assertThat(allCategories.size(), is(1));
		assertThat(allCategories.get(0), is(equalTo(cat)));
		assertThat(allCategories.get(0), is(equalTo(cat2)));
		assertThat(allCategories.get(0), is(equalTo(cat3)));
	}
	
	@Test
	public void testCreateCategory_repeatedName_saveStation() throws TunerPersistenceException {
		Station s = new Station("test", "http://test");
		
		Category cat = new Category("Rock 100%");
		cat.getStations().add(s);
		cat = catController.createCategory(cat);
		
		s.getCategories().add(cat);
		s = stationController.saveStation(s);
		
		
		Station s2 = new Station("test2", "http://test2");
		Category cat2 = new Category("Rock 100%");
		cat2.getStations().add(s2);
		cat2 = catController.createCategory(cat2);
		
		s2.getCategories().add(cat2);
		s2 = stationController.saveStation(s2);
		
		Station s3 = new Station("test3", "http://test3");
		Category cat3 = new Category("Rock 100%");
		cat3.getStations().add(s3);
		cat3 = catController.createCategory(cat3);
		
		s3.getCategories().add(cat3);
		s3 = stationController.saveStation(s3);
		
		// Assert
		List<Category> allCategories = catController.getAllCategories(); 
		assertThat(allCategories.size(), is(1));
		assertThat(allCategories.get(0).getStations().size(), is(3));
	}
	
	@Test
	public void testDeleteUnexistent() throws TunerPersistenceException {
		Category cat = new Category("C1");
		cat = catController.createCategory(cat);
		
		Category cat1 = new Category("C2");
		cat1 = catController.createCategory(cat1);
		
		Category cat2 = new Category("Rock 100%");
		catController.deleteCategory(cat2);
		
		// Assert
		List<Category> allCategories = catController.getAllCategories(); 
		assertThat(allCategories.size(), is(2));
		assertThat(allCategories, containsInAnyOrder(cat, cat1));
	}

}
