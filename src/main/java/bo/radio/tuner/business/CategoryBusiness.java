package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.CategoryDaoApi;
import bo.radio.tuner.dao.CategoryDao;
import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.entities.Category;
import bo.radio.tuner.exceptions.TunerPersistenceException;
import bo.radio.tuner.utils.LogUtils;

public class CategoryBusiness extends AbstractCrudBusiness<Category> implements CategoryDaoApi {

	private final static Logger log = LoggerFactory.getLogger(CategoryBusiness.class);

	CategoryBusiness(String databaseUrl) {
		super(databaseUrl);
	}
	
	/**
	 * Save a Category only if there is no other Category with the same name.
	 */
	@Override
	public Category createCategory(Category category) throws TunerPersistenceException {
		log.info("Saving {}", category);
		try(CrudDao<Category> dao = createDao()) {
			return create(category, () -> dao.getEntitiesByColumn(Category.NAMECOLUMN_NAME, category.getName()));
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
	public Optional<Category> getCategory(int id) throws TunerPersistenceException {
		log.info("Fetching Category with id={}", id);
		try {
			Category category = getById(id);
			LogUtils.logDebug(log, () -> String.format("Category found [%s]", category));
			return Optional.ofNullable(category);
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
	
	@Override
	public Optional<Category> findCategoryByName(String name) throws TunerPersistenceException {
		try {
			return Optional.ofNullable(getByName(name));
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching a Category.", e);
		}
	}
	
	@Override
	protected String getNameColumn() {
		return Category.NAMECOLUMN_NAME;
	}
}
