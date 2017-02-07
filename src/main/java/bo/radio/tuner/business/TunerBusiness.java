package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.Properties;

import bo.radio.tuner.CategoryDaoApi;
import bo.radio.tuner.StationDaoApi;
import bo.radio.tuner.TunerDaoApi;
import bo.radio.tuner.dao.TunerDaoUtils;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public class TunerBusiness implements TunerDaoApi {
	private static final String DATABASEURL_KEY = "database.url";
	private static final String STATIONPATH_KEY = "station.path";

	private final String databaseUrl;
	private final String serializedStationPath;
	private static StationDaoApi stationController = null;
	private static CategoryDaoApi categoryController = null;

	public TunerBusiness(Properties databaseProperties) {
		String dbUrl = databaseProperties.getProperty(DATABASEURL_KEY);
		String stationPath = databaseProperties.getProperty(STATIONPATH_KEY);

		if (dbUrl == null || dbUrl.isEmpty()) {
			throw new RuntimeException(String.format(
					"No DB URL found in the property file. Check that the key [%s] has a valid value.",
					DATABASEURL_KEY));
		}
		
		if (stationPath == null || stationPath.isEmpty()) {
			throw new RuntimeException(String.format(
					"No File path found in the property file to serialize a Station. Check that the key [%s] has a valid value.",
					STATIONPATH_KEY));
		}

		this.databaseUrl = dbUrl;
		this.serializedStationPath = stationPath;
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
	public StationDaoApi getStationControllerInstance() {
		if (stationController == null) {
			stationController = new StationBusiness(databaseUrl, serializedStationPath);
		}
		return stationController;
	}

	@Override
	public CategoryDaoApi getCategoryControllerInstance() {
		if (categoryController == null) {
			categoryController = new CategoryBusiness(databaseUrl);
		}

		return categoryController;
	}

}
