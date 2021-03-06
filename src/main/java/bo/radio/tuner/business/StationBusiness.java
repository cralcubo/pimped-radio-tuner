package bo.radio.tuner.business;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.tuner.StationDaoApi;
import bo.radio.tuner.dao.CrudDao;
import bo.radio.tuner.dao.StationDao;
import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;
import bo.radio.tuner.utils.LogUtils;
import bo.radio.tuner.utils.SerializeObjectUtils;

public class StationBusiness extends AbstractCrudBusiness<Station> implements StationDaoApi {
	private final static Logger log = LoggerFactory.getLogger(StationBusiness.class);
	private final SerializeObjectUtils<Station> stationSerializer;
	
	private final String serializedStationPath; 

	StationBusiness(String databaseUrl, String serializedStationPath) {
		super(databaseUrl);
		this.serializedStationPath = serializedStationPath;
		File destinationFile = new File(serializedStationPath);
		if (!destinationFile.exists()) {
			try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("There was an error creating the File:" + serializedStationPath
						+ ". Make sure that there is enough right to create a file in that folder.");
			}
		}
		stationSerializer = new SerializeObjectUtils<>(destinationFile, Station.class);
	}

	/**
	 * Save a Station only if the streamUrl is different from the ones already
	 * saved. 
	 */
	@Override
	public Station saveStation(Station station) throws TunerPersistenceException {
		log.info("Saving {}", station);
		try (final CrudDao<Station> dao = createDao()){
			return create(station, () -> dao.getEntitiesByColumn(Station.STREAMCOLUMN_NAME, station.getStream()));
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error connecting to the database.", e);
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
	public Optional<Station> getStationById(int id) throws TunerPersistenceException {
		log.info("Fetching Station with id={}", id);
		try {
			Station station = getById(id);
			LogUtils.logDebug(log, () -> String.format("Station found [%s]", station));
			return Optional.ofNullable(station);
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching a Station.", e);
		}
	}
	
	@Override
	public Optional<Station> findStation(Station station) throws TunerPersistenceException {
		if (station.getStream() == null || station.getStream().trim().isEmpty()) {
			throw new IllegalArgumentException("The Station object must have a streamUrl to search for.");
		}
	
		try(CrudDao<Station> dao = createDao()) {
			List<Station> similarStations = dao.getEntitiesByColumn(Station.STREAMCOLUMN_NAME, station.getStream());
			if(similarStations.isEmpty()) {
				return Optional.empty();
			} else if(similarStations.size() > 1) {
				throw new RuntimeException("More than one Station has the same StreamUrl, this is totally unexpected.");
			}
			
			return Optional.of(similarStations.get(0));
		} catch (SQLException e) {
			throw new TunerPersistenceException(String.format("There was an error fetching similar stations to [%s]", station), e);
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
	
	@Override
	public Optional<Station> findStationByName(String name) throws TunerPersistenceException {
		try(CrudDao<Station> dao = createDao()) {
			return Optional.ofNullable(getByName(name));
		} catch (SQLException e) {
			throw new TunerPersistenceException("There was an error fetching a Station.", e);
		}
	}
	
	@Override
	protected String getNameColumn() {
		return Station.NAMECOLUMN_NAME;
	}

	@Override
	public Optional<Station> getLastPlayedStation() throws TunerPersistenceException  {
		try {
			return Optional.of(stationSerializer.read());
		} catch (ClassNotFoundException | IOException e) {
			throw new TunerPersistenceException("There was an error reading the serialized Station from: " + serializedStationPath, e);
		}
	}

	@Override
	public void saveLastPlayedStation(Station s) throws TunerPersistenceException {
		try {
			stationSerializer.write(s);
		} catch (IOException e) {
			throw new TunerPersistenceException("There was an error writing the serialized Station to: " + serializedStationPath, e);
		}
	}

}
