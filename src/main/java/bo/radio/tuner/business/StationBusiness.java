package bo.radio.tuner.business;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.StationController;
import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.dao.StationDao;
import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;
import bo.radio.tuner.utils.LogUtils;

public class StationBusiness extends AbstractCrudBusiness<Station> implements StationController {
	private final static Logger log = LoggerFactory.getLogger(StationBusiness.class);

	StationBusiness(String databaseUrl) {
		super(databaseUrl);
	}

	@Override
	public Station saveStation(Station station) throws TunerPersistenceException {
		log.info("Saving {}", station);
		try {
			return create(station, station.getName());
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error saving a Station.", e);
		}
	}

	@Override
	public void updateStation(Station station) throws TunerPersistenceException {
		log.info("Updating {}", station);
		try {
			update(station);
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error updating a Station.", e);
		}
	}

	@Override
	public void removeStation(Station station) throws TunerPersistenceException {
		log.info("Deleting {}", station);
		try {
			delete(station);
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error deleting a Station.", e);
		}
	}

	@Override
	public Station getStation(int id) throws TunerPersistenceException {
		log.info("Fetching Station with id={}", id);
		try {
			Station station = getById(id);
			LogUtils.logDebug(log, () -> String.format("Station found [%s]", station));
			return station;
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching a Station.", e);
		}
	}

	@Override
	public List<Station> getAllStations() throws TunerPersistenceException {
		try {
			return getAll();
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching all Stations.", e);
		}
	}

	@Override
	protected CrudDao<Station> createDao() throws SQLException {
		return new StationDao(databaseUrl);
	}

}
