package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.CategoryStation;
import bo.radio.tuner.entities.Station;

public class CategoryDao extends ManyToManyDao<Category, Station, CategoryStation> {
	
	protected CategoryDao(String dbUrl) throws SQLException {
		super(Category.class, Station.class, CategoryStation.class, dbUrl);
	}
	
	public CategoryDao() throws SQLException {
		super(Category.class, Station.class, CategoryStation.class);
	}
	
	@Override
	protected List<Station> getMany(Category o) {
		return o.getStations();
	}
	
	@Override
	protected BiFunction<Category, Station, CategoryStation> joinFactory() {
		return (c, s) -> new CategoryStation(s, c);
	}
	
	@Override
	protected void addMany(Category o, Station m) {
		o.getStations().add(m);
	}

	@Override
	protected String getJoinOneColumnName() {
		return CategoryStation.CATEGORYCOL_NAME;
	}

	@Override
	protected String getJoinManyColumnName() {
		return CategoryStation.STATIONCOL_NAME;
	}

	@Override
	protected String getOneColumnIdName() {
		return Category.IDCOLUMN_NAME;
	}

}
