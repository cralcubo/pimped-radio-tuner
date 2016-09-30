package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;

import bo.radio.tuner.entities.Category;
import bo.radio.tuner.entities.CategoryStation;
import bo.radio.tuner.entities.Station;

public class CategoryDao extends OneToManyDao<Category, Station, CategoryStation> {
	
	public CategoryDao(String dbUrl) throws SQLException {
		super(Category.class, Station.class, CategoryStation.class, dbUrl);
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
		m.getCategories().add(o);
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
	
	@Override
	protected String getOneColumnName() {
		return Category.NAMECOLUMN_NAME;
	}

}
