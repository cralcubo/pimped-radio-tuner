package bo.radio.tuner;

import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface TunerDaoApi {
	/**
	 * Initialize the database that will store
	 * stations and categories.
	 */
	void init() throws TunerPersistenceException;
	
	/**
	 * Instantiate or fetch a StationController
	 */
	StationDaoApi getStationControllerInstance();
	
	/**
	 * Instantiate or fetch a CategoryController 
	 */
	CategoryDaoApi getCategoryControllerInstance();

}
