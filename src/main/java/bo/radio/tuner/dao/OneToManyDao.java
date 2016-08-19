package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

public abstract class OneToManyDao<O, M, J> implements CrudDao<O> {
	private final ConnectionSource connectionSource;

	private Dao<O, Integer> oneDao;
	private Dao<M, Integer> manyDao;
	private Dao<J, Integer> joinDao;

	protected OneToManyDao(Class<O> one, Class<M> many, Class<J> join, String databaseUrl) throws SQLException {
		connectionSource = new JdbcConnectionSource(databaseUrl);
		this.oneDao = DaoManager.createDao(connectionSource, one);
		this.manyDao = DaoManager.createDao(connectionSource, many);
		this.joinDao = DaoManager.createDao(connectionSource, join);
	}
	
	/* *** CREATE *** */
	
	public O create(O one) throws SQLException {
		O o = oneDao.createIfNotExists(one);
		
		for (M m : getMany(one)) {
			m = manyDao.createIfNotExists(m);
			joinDao.createIfNotExists(joinFactory().apply(o, m));
		}

		return o;
	}
	
	protected abstract List<M> getMany(O o);
	protected abstract BiFunction<O, M, J> joinFactory();
	
	/* *** UPDATE *** */
	
	public void update(O o) throws SQLException {
		oneDao.update(o);
		
		List<M> addElements = findDifference(getMany(o), queryMany(o));
		List<M> removeElements = findDifference(queryMany(o), getMany(o));
		
		// Add new elements
		for(M m : addElements) {
			manyDao.createIfNotExists(m);
			joinDao.createIfNotExists(joinFactory().apply(o, m));
		}
		// Remove old elements
		QueryBuilder<J, Integer> joinTableQuery = joinDao.queryBuilder();
		
		for(M m : removeElements) {
			Where<J, Integer> qb = joinTableQuery.where().eq(getJoinOneColumnName(), o).and().eq(getJoinManyColumnName(), m);
			for(J j : qb.query()) {
				joinDao.delete(j);
			}
		}
	}
	
	/* *** DELETE *** */
	
	public void delete(O one) throws SQLException {
		// Delete references in the join table
		for(J j : joinDao.queryForEq(getJoinOneColumnName(), one)) {
			joinDao.delete(j);
		}
		// Delete the object 
		oneDao.delete(one);
	}
	
	/* *** READ *** */
	
	public O getById(int id) throws SQLException {
		O o = oneDao.queryForId(id);
		// Get all Many elements 
		queryMany(o).forEach(m -> addMany(o, m));
		
		return o;
	}
	
	public List<O> getEntitiesByColumn(String columnName, Object value) throws SQLException {
		List<O> ones = oneDao.queryForEq(columnName, value);
		for (O o : ones) {
			queryMany(o).forEach(m -> addMany(o, m));
		}

		return ones;
	}
	
	protected abstract void addMany(O o, M m);
	
	public List<O> getAll() throws SQLException {
		List<O> all = oneDao.queryForAll();
		
		for(O o : all) {
			queryMany(o).forEach(m -> addMany(o, m));
		}
		
		return all; 
	}
	
	public List<M> queryMany(O one) throws SQLException {
		PreparedQuery<M> getManyQuery = preparedGetManyQuery(getJoinOneColumnName(),
															getJoinManyColumnName(), 
															getOneColumnIdName());
		getManyQuery.setArgumentHolderValue(0, one);

		return manyDao.query(getManyQuery);
	}
	
	protected abstract String getJoinOneColumnName();
	protected abstract String getJoinManyColumnName();
	protected abstract String getOneColumnIdName();
	protected abstract String getOneColumnName();

	public void close() {
		connectionSource.closeQuietly();
	}
	
	/* *** UTILS *** */
	
	/**
	 * Create a prepared query for a Many to Many relation.
	 * 
	 * The Join table is the one that contains the foreign ID's of 
	 * the tables to be queried. 
	 * 
	 * The Target table is the one that finally will be queried
	 * to get a list of the elements needed.
	 * 
	 * @param oneColumn is the name of the column in the Join table from which Many elements can be queried.
	 * @param manyColumn is the name of the column in the Join table that contains the ID's of the Many elements to be queried.
	 * @param targetIdColumn is the ID of the Target table.
	 * @return
	 * @throws SQLException
	 */
	private PreparedQuery<M> preparedGetManyQuery(String oneColumn, String manyColumn, String targetIdColumn) throws SQLException {
		QueryBuilder<J, Integer> joinTableQuery = joinDao.queryBuilder();
		joinTableQuery.selectColumns(manyColumn);
		SelectArg selectArg = new SelectArg();
		joinTableQuery.where().eq(oneColumn, selectArg);
		
		QueryBuilder<M, Integer> targetQuery = manyDao.queryBuilder();
		targetQuery.where().in(targetIdColumn, joinTableQuery);
		
		return targetQuery.prepare();
	}
	
	private List<M> findDifference(List<M> originalList, List<M> newList) {
		List<M> diff = new ArrayList<>(originalList);
		diff.removeAll(newList);
		return diff;
	}

}
