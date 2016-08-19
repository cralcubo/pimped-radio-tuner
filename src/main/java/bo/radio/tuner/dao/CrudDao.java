package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T> extends AutoCloseable {
	
	T create(T t) throws SQLException;
	
	void update(T t) throws SQLException;
	
	void delete(T t) throws SQLException;
	
	T getById(int id) throws SQLException;
	
	List<T> getEntitiesByColumn(String columnName, Object entityValue) throws SQLException;
	
	List<T> getAll() throws SQLException;
	
	void close();

}
