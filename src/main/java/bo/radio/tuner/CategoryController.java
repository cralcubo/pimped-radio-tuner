package bo.radio.tuner;

import java.util.List;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface CategoryController {
	
	Category createCategory(Category category) throws TunerPersistenceException;
	
	void updateCategory(Category category) throws TunerPersistenceException;
	
	void deleteCategory(Category category) throws TunerPersistenceException;
	
	Category getCategory(int id) throws TunerPersistenceException;
	
	List<Category> getAllCategories() throws TunerPersistenceException;
	
}
