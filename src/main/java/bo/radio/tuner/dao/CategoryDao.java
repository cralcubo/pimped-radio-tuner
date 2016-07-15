package bo.radio.tuner.dao;

import java.sql.SQLException;
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
	protected BiFunction<Category, Station, CategoryStation> instantiateJoin() {
		return (c, s) -> new CategoryStation(s, c);
	}

}
