package bo.radio.tuner;

import java.util.List;

import bo.radio.tuner.entities.Station;
import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface StationController {
	
	Station saveStation(Station station) throws TunerPersistenceException;
	
	void updateStation(Station station) throws TunerPersistenceException;
	
	void removeStation(Station station) throws TunerPersistenceException;
	
	Station getStation(int id) throws TunerPersistenceException;
	
	List<Station> getAllStations() throws TunerPersistenceException;

}
