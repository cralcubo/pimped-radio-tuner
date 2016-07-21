package bo.radio.tuner.exceptions;

public class TunerPersistenceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final static String DEFAULT_MSG = "There was an error interacting with the Tuner.";
	
	public TunerPersistenceException(String msg, Exception e) {
		super(msg, e);
	}
	
	public TunerPersistenceException(String msg) {
		super(msg);
	}
	
	public TunerPersistenceException() {
		this(DEFAULT_MSG);
	}

}
