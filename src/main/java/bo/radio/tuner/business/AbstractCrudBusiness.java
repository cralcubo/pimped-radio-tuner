package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.utils.LogUtils;

public abstract class AbstractCrudBusiness<T> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCrudBusiness.class);
	
	protected final String databaseUrl;
	
	protected abstract CrudDao<T> createDao() throws SQLException;
	
	protected AbstractCrudBusiness(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	protected T create(T t, String entityName) throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			LogUtils.logDebug(log, () -> String.format("Checking if there are more entities with name [%s]", entityName));
			List<T> entities = dao.getByName(entityName);
			LogUtils.logDebug(log, () -> String.format("Entities found [%s]", entities));

			if (entities.isEmpty()) {
				log.info("Creating {}", t);
				return dao.create(t);
			}

			if (entities.size() == 1) {
				T e = entities.get(0);
				log.info("Retourning {}", e);
				return e;
			}

			throw new RuntimeException(String.format(
					"Something totally unexpected happend. Entities with the name [%s] found were [%d], when Entities with the same name are not allowed.",
					entityName, entities.size()));
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

	protected List<T> getAll() throws SQLException {
		try (CrudDao<T> dao = createDao()) {
			return dao.getAll();
		}
	}

}
