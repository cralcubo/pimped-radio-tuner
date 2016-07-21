package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.Properties;

import bo.radio.tuner.CategoryController;
import bo.radio.tuner.StationController;
import bo.radio.tuner.TunerController;
import bo.radio.tuner.dao.TunerDaoUtils;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public class TunerBusiness implements TunerController {
	private static final String DATABASEURL_KEY = "database.url";

	private final String databaseUrl;
	private static StationController stationController = null;
	private static CategoryController categoryController = null;

	public TunerBusiness(Properties databaseProperties) {
		String dbUrl = databaseProperties.getProperty(DATABASEURL_KEY);

		if (dbUrl == null || dbUrl.isEmpty()) {
			throw new RuntimeException(String.format(
					"No DB URL found in the property file. Check that the key [%s] has a valid value in the file.",
					DATABASEURL_KEY));
		}

		this.databaseUrl = dbUrl;
	}

	@Override
	public void init() throws TunerPersistenceException {
		try {
			TunerDaoUtils.createAllTables(databaseUrl);
		} catch (SQLException e) {
			throw new TunerPersistenceException(
					"There was an error creating the the Turner tables in DB=" + databaseUrl, e);
		}
	}

	@Override
	public StationController getStationControllerInstance() {
		if (stationController == null) {
			stationController = new StationBusiness(databaseUrl);
		}

		return stationController;
	}

	@Override
	public CategoryController getCategoryControllerInstance() {
		if (categoryController == null) {
			categoryController = new CategoryBusiness(databaseUrl);
		}

		return categoryController;
	}

}
