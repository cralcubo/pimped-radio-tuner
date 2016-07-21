package bo.radio.tuner.business;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Test;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public class CategoryStationTest extends TunerBusinessTest {
	
	@Test
	public void testCreateCategory() throws TunerPersistenceException{
		Category cat = new Category("test");
		cat = catController.createCategory(cat);
		
		// Assert
		Category fCat = catController.getCategory(cat.getId());
		
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
		Category fCat = catController.getCategory(cat.getId());
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
		Category cat = new Category("Rock 100%");
		cat = catController.createCategory(cat);
		
		Category cat2 = new Category("Rock 100%");
		cat2 = catController.createCategory(cat2);
		
		// Assert
		List<Category> allCategories = catController.getAllCategories(); 
		assertThat(allCategories.size(), is(1));
		assertThat(allCategories.get(0), is(equalTo(cat)));
		assertThat(allCategories.get(0), is(equalTo(cat2)));
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
