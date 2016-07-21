package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.CategoryController;
import bo.radio.tuner.dao.CategoryDao;
import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.entities.Category;
import bo.radio.tuner.exceptions.TunerPersistenceException;
import bo.radio.tuner.utils.LogUtils;

public class CategoryBusiness extends AbstractCrudBusiness<Category> implements CategoryController {

	private final static Logger log = LoggerFactory.getLogger(CategoryBusiness.class);

	CategoryBusiness(String databaseUrl) {
		super(databaseUrl);
	}

	@Override
	public Category createCategory(Category category) throws TunerPersistenceException {
		log.info("Saving {}", category);
		try {
			return create(category, category.getName());
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error saving a Category.", e);
		}
	}

	@Override
	public void updateCategory(Category category) throws TunerPersistenceException {
		log.info("Updating {}", category);
		try {
			update(category);
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error updating a Category.", e);
		}
	}

	@Override
	public void deleteCategory(Category category) throws TunerPersistenceException {
		log.info("Deleting {}", category);
		try {
			delete(category);
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error deleting a Category.", e);
		}

	}

	@Override
	public Category getCategory(int id) throws TunerPersistenceException {
		log.info("Fetching Category with id={}", id);
		try {
			Category category = getById(id);
			LogUtils.logDebug(log, () -> String.format("Category found [%s]", category));
			
			return category;
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching a Category.", e);
		}
	}

	@Override
	public List<Category> getAllCategories() throws TunerPersistenceException {
		try {
			return getAll();
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching all Categories.", e);
		}
	}

	@Override
	protected CrudDao<Category> createDao() throws SQLException {
		return new CategoryDao(databaseUrl);
	}
}
