package bo.radio.tuner;

import java.util.List;
import java.util.Optional;

import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface StationDaoApi {
	
	Station saveStation(Station station) throws TunerPersistenceException;
	
	void updateStation(Station station) throws TunerPersistenceException;
	
	void removeStation(Station station) throws TunerPersistenceException;
	
	Optional<Station> getStationById(int id) throws TunerPersistenceException;
	
	Optional<Station> findStation(Station station) throws TunerPersistenceException;
	
	List<Station> getAllStations() throws TunerPersistenceException;

	Optional<Station> findStationByName(String name) throws TunerPersistenceException;
	
	Optional<Station> getLastPlayedStation() throws TunerPersistenceException;
	
	void saveLastPlayedStation(Station station) throws TunerPersistenceException;

}
