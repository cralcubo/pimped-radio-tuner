package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import bo.radio.tuner.utils.PropertiesLoader;

public abstract class ManyToManyDao<O, M, J> {
	private final ConnectionSource connectionSource;

	private Dao<O, Integer> oneDao;
	private Dao<M, Integer> manyDao;
	private Dao<J, Integer> joinDao;
	
	protected ManyToManyDao(Class<O> one, Class<M> many, Class<J> join) throws SQLException {
		this(one, many, join, PropertiesLoader.getProperty("database.url"));
	}

	protected ManyToManyDao(Class<O> one, Class<M> many, Class<J> join, String databaseUrl) throws SQLException {
		connectionSource = new JdbcConnectionSource(databaseUrl);
		this.oneDao = DaoManager.createDao(connectionSource, one);
		this.manyDao = DaoManager.createDao(connectionSource, many);
		this.joinDao = DaoManager.createDao(connectionSource, join);
	}
	
	/* *** CREATE *** */
	
	public O create(O one) throws SQLException {
		O o = oneDao.createIfNotExists(one);
		
		for (M m : getMany(one)) {
			manyDao.createIfNotExists(m);
			joinDao.createIfNotExists(joinFactory().apply(o, m));
		}

		return o;
	}
	
	protected abstract List<M> getMany(O o);
	protected abstract BiFunction<O, M, J> joinFactory();
	
		
	
	/* *** UPDATE *** */
	
	public void update(O o) throws SQLException {
		oneDao.update(o);
		
		List<M> newManies = getMany(o);
		List<M> oldManies = queryMany(o);
		
		
		
	}
	
	/* *** DELETE *** */
	
	public void delete(O one) throws SQLException {
		// Delete references in the join table
		PreparedQuery<J> joinElementsQuery = prepareGetJoinElementsQuery(getJoinOneColumnName());
		joinElementsQuery.setArgumentHolderValue(0, one);
		
		for(J j : joinDao.query(joinElementsQuery)) {
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
	 * @param joinDao is the DAO of the Join table.
	 * @param oneColumn is the name of the column in the Join table from which Many elements can be queried.
	 * @param manyColumn is the name of the column in the Join table that contains the ID's of the Many elements to be queried.
	 * @param targetDao is the Target table that will return the list of elements queried.
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
	
	/**
	 * Get all the elements from the Join Table.
	 * 
	 * Select * From JoinTable where conditionColumn = ?
	 * 
	 * @param conditionColumn is the column that will be used to satisfy the where condition.
	 * @return
	 * @throws SQLException
	 */
	private PreparedQuery<J> prepareGetJoinElementsQuery(String conditionColumn) throws SQLException {
		QueryBuilder<J, Integer> joinTableQuery = joinDao.queryBuilder();
		SelectArg selectArg = new SelectArg();
		joinTableQuery.where().eq(conditionColumn, selectArg);
		
		return joinTableQuery.prepare();
	}

}
