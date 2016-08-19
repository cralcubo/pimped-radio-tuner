package bo.radio.tuner;

import bo.radio.tuner.exceptions.TunerPersistenceException;

public interface TunerDaoController {
	/**
	 * Initialize the database that will store
	 * stations and categories.
	 */
	void init() throws TunerPersistenceException;
	
	/**
	 * Instantiate or fetch a StationController
	 */
	StationDaoController getStationControllerInstance();
	
	/**
	 * Instantiate or fetch a CategoryController 
	 */
	CategoryDaoController getCategoryControllerInstance();

}
