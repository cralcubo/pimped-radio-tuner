package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.utils.DaoSupplier;
import bo.radio.tuner.utils.LogUtils;

public abstract class AbstractCrudBusiness<T> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCrudBusiness.class);
	
	protected final String databaseUrl;
	
	protected abstract CrudDao<T> createDao() throws SQLException;
	
	protected AbstractCrudBusiness(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
	
	/**
	 * Save an entity in the databases, only if that entity
	 * does not exist yet.
	 * 
	 * @param t entity to save.
	 * @param queryEntities supply a list with a search of all entities that match the entity that will be saved. 
	 * @return the saved entity.
	 * @throws SQLException
	 */
	protected T create(T t, DaoSupplier<List<T>> queryEntities) throws SQLException {
		LogUtils.logDebug(log, () -> "Checking if there are already created entities");
		List<T> entities = queryEntities.get();
		LogUtils.logDebug(log, () -> String.format("Entities found [%s]", entities));
		
		try (CrudDao<T> dao = createDao()) {
			if (entities.isEmpty()) {
				log.info("Creating {}", t);
				return dao.create(t);
			}

			if (entities.size() == 1) {
				T e = entities.get(0);
				log.info("Retourning {}", e);
				return e;
			}

			throw new RuntimeException("Something totally unexpected happend. More than one entity found.");
		}
	}

	protected void update(T t) throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			dao.update(t);
		}
	}

	protected void delete(T t) throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			dao.delete(t);
		}
	}

	protected T getById(int id) throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			return dao.getById(id);
		}
	}
	
	protected T getByName(String name) throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			List<T> entities = dao.getEntitiesByColumn(getNameColumn(), name);
			if(entities.isEmpty()) {
				return null;
			}
			if(entities.size() > 1) {
				throw new RuntimeException("Something totally unexpected happend. More than one entity found.");
			}
			return entities.get(0);
		}
	}

	protected List<T> getAll() throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			return dao.getAll();
		}
	}
	
	protected abstract String getNameColumn();

}
