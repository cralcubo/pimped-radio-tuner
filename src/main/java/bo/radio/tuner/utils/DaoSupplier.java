package bo.radio.tuner.utils;

import java.sql.SQLException;

@FunctionalInterface
public interface DaoSupplier<T> {
	
	T get() throws SQLException;

}
