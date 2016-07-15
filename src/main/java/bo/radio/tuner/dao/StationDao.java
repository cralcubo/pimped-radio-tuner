package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.function.BiFunction;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.CategoryStation;
import bo.radio.tuner.entities.Station;

public class StationDao extends ManyToManyDao<Station, Category, CategoryStation>{
	
	public StationDao() throws SQLException {
		super(Station.class, Category.class, CategoryStation.class);
	}
	
	public StationDao(String databaseUrl) throws SQLException {
		super(Station.class, Category.class, CategoryStation.class, databaseUrl);
	}

	@Override
	protected String getJoinOneColumnName() {
		return CategoryStation.STATIONCOL_NAME;
	}

	@Override
	protected String getJoinManyColumnName() {
		return CategoryStation.CATEGORYCOL_NAME;
	}

	@Override
	protected String getOneColumnIdName() {
		return Station.IDCOLUMN_NAME;
	}

	@Override
	protected BiFunction<Station, Category, CategoryStation> instantiateJoin() {
		return CategoryStation::new;
	}

}
