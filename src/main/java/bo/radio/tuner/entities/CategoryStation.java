package bo.radio.tuner.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "category_station")
public class CategoryStation {
	public final static String CATEGORYCOL_NAME = "category_id";
	public final static String STATIONCOL_NAME = "station_id";

	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField(foreign = true, columnName = STATIONCOL_NAME)
	Station station;
	
	@DatabaseField(foreign = true, columnName = CATEGORYCOL_NAME)
	Category category;
	
	public CategoryStation() {
		// nothing here
	}

	public CategoryStation(Station station, Category category) {
		this.station = station;
		this.category = category;
	}

}
