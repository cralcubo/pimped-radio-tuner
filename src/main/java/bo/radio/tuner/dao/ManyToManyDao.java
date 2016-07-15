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

	public O create(O one, List<M> manies) throws SQLException {
		O o = oneDao.createIfNotExists(one);
		
		for (M m : manies) {
			manyDao.createIfNotExists(m);
			joinDao.createIfNotExists(instantiateJoin().apply(o, m));
		}

		return o;
	}
	
	protected abstract BiFunction<O, M, J> instantiateJoin();
	
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

	public O getById(int id) throws SQLException {
		return oneDao.queryForId(id);
	}
	
	public List<O> getAllOnes() throws SQLException {
		return oneDao.queryForAll();
	}

	public List<M> getMany(O one) throws SQLException {
		PreparedQuery<M> getManyQuery = preparedGetManyQuery(joinDao, 
															getJoinOneColumnName(), 
															getJoinManyColumnName(), 
															manyDao,
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
	private PreparedQuery<M> preparedGetManyQuery(Dao<J, Integer> joinDao, String oneColumn, String manyColumn, Dao<M, Integer> targetDao,  String targetIdColumn) throws SQLException {
		QueryBuilder<J, Integer> joinTableQuery = joinDao.queryBuilder();
		joinTableQuery.selectColumns(manyColumn);
		SelectArg selectArg = new SelectArg();
		joinTableQuery.where().eq(oneColumn, selectArg);
		
		QueryBuilder<M, Integer> targetQuery = targetDao.queryBuilder();
		targetQuery.where().in(targetIdColumn, joinTableQuery);
		
		return targetQuery.prepare();
	}
	
	private PreparedQuery<J> prepareGetJoinElementsQuery(String oneColumn) throws SQLException {
		QueryBuilder<J, Integer> joinTableQuery = joinDao.queryBuilder();
		SelectArg selectArg = new SelectArg();
		joinTableQuery.where().eq(oneColumn, selectArg);
		
		return joinTableQuery.prepare();
	}
}
