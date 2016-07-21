package bo.radio.tuner;

import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface TunerController {
	/**
	 * Initialize the database that will store
	 * stations and categories.
	 */
	void init() throws TunerPersistenceException;
	
	/**
	 * Instantiate or fetch a StationController
	 */
	StationController getStationControllerInstance();
	
	/**
	 * Instantiate or fetch a CategoryController 
	 */
	CategoryController getCategoryControllerInstance();

}
