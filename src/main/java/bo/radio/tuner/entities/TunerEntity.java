package bo.radio.tuner.entities;

import com.j256.ormlite.field.DatabaseField;

public class TunerEntity {
	public static final String IDCOLUMN_NAME = "id";
	
	@DatabaseField(columnName= IDCOLUMN_NAME, generatedId = true)
	protected int id;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TunerEntity other = (TunerEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
