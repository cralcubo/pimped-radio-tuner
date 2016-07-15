package bo.radio.tuner.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

public class TunerDaoUtils {
	private static final String ENTITIES_PKG = "bo.radio.tuner.entities";
	
	private static final Set<Class<?>> ENTITIES;
	static {
		Reflections reflections = new Reflections(ENTITIES_PKG);
		ENTITIES = reflections.getTypesAnnotatedWith(DatabaseTable.class);
	}
	
	/**
	 * Create all the tables located in the
	 * package {@code bo.radio.tuner.entities}
	 * 
	 * @param databaseUrl
	 * @throws SQLException
	 */
	public static void createAllTables(String databaseUrl) throws SQLException {
		ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
		
		for (Class<?> e : ENTITIES) {
			TableUtils.createTableIfNotExists(connectionSource, e);
		}
		
		connectionSource.close();
	}
	
	/**
	 * Delete all the tables located in the
	 * 
	 * package {@code bo.radio.tuner.entities}
	 *  
	 * @param databaseUrl
	 * @throws SQLException
	 */
	public static void deleteAllTables(String databaseUrl) throws SQLException {
		ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
		
		for (Class<?> e : ENTITIES) {
			TableUtils.dropTable(connectionSource, e, true);
		}
		
		connectionSource.close();
	}
	
	/**
	 * Get the name of all the tables annotated 
	 * in the package {@code bo.radio.tuner.entities}
	 * 
	 * @return
	 */
	public static List<String> getAllTableNames() {
		return ENTITIES.stream()
				.map(e -> e.getAnnotation(DatabaseTable.class).tableName())
				.collect(Collectors.toList());
	}

}
