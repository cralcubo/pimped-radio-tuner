package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;
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
	protected List<Category> getMany(Station o) {
		return o.getCategories();
	}
	
	@Override
	protected BiFunction<Station, Category, CategoryStation> joinFactory() {
		return CategoryStation::new;
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
	protected void addMany(Station o, Category m) {
		o.getCategories().add(m);
	}

}
