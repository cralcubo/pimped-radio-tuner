package bo.radio.tuner;

import java.util.List;
import java.util.Optional;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface CategoryDaoApi {
	
	Category createCategory(Category category) throws TunerPersistenceException;
	
	void updateCategory(Category category) throws TunerPersistenceException;
	
	void deleteCategory(Category category) throws TunerPersistenceException;
	
	Optional<Category> getCategory(int id) throws TunerPersistenceException;
	
	List<Category> getAllCategories() throws TunerPersistenceException;

	Optional<Category> findCategoryByName(String name) throws TunerPersistenceException;
	
}
