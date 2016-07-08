package bo.radio.tuner.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class TunerDao<T> {
	private final ConnectionSource connectionSource;
	private final Dao<T, String> daoManager;

	public TunerDao(final String databaseUrl, Class<T> t) throws SQLException {
		connectionSource = new JdbcConnectionSource(databaseUrl);
		daoManager = DaoManager.createDao(connectionSource, t);
	}

	public void save(T entity) throws SQLException {
		daoManager.create(entity);
	}

	public T read(String id) throws SQLException {
		return daoManager.queryForId(id);
	}

	public void update(T entity) {
		// TODO Auto-generated method stub

	}

	public void delete(T entity) {
		// TODO Auto-generated method stub

	}

	public void close() {
		connectionSource.closeQuietly();
	}
}
