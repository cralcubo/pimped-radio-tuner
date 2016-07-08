package bo.radio.tuner.business;

import java.sql.SQLException;

import bo.radio.tuner.dao.TunerDao;
import bo.radio.tuner.model.Station;

public class StationManager {
	
	private static final String DATABSE_URL = null;
	private final TunerDao<Station> stationDao;
	
	public StationManager() throws SQLException {
		stationDao = new TunerDao<>(DATABSE_URL, Station.class);
	}
	
	public void saveStation(Station station) throws SQLException {
		stationDao.save(station);
		stationDao.close();
	}

}
