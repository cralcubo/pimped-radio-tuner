package bo.radio.tuner.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category {
	public static final String IDCOLUMN_NAME = "id";
	
	@DatabaseField(columnName= IDCOLUMN_NAME, generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	
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
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}
