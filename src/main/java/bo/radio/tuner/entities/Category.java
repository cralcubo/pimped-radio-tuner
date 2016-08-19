package bo.radio.tuner.entities;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category extends TunerEntity {
	public static final String NAMECOLUMN_NAME = "name";

	@DatabaseField(columnName=NAMECOLUMN_NAME)
	private String name;
	
	private List<Station> stations = new ArrayList<>();
	
	public Category() {
		// Empty for ORM Lite
	}

	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
	
	public List<Station> getStations() {
		return stations;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", stations=" + stations + ", id=" + id + "]";
	}
	
}
