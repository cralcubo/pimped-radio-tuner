package bo.radio.tuner.entities;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stations")
public class Station extends TunerEntity {

	public static final String NAMECOLUMN_NAME = "name";
	
	@DatabaseField
	private String stream;
	@DatabaseField(columnName = NAMECOLUMN_NAME)
	private String name;
	@DatabaseField
	private float bitRate;
	@DatabaseField
	private float sampleRate;
	@DatabaseField
	private String codec;

	private List<Category> categories = new ArrayList<>();

	public Station() {
		// empty for ORM lite
	}

	public Station(String name, String stream) {
		this.name = name;
		this.stream = stream;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public float getBitRate() {
		return bitRate;
	}

	public void setBitRate(float bitRate) {
		this.bitRate = bitRate;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getCategories() {
		return categories;
	}
	
	public String getCodec() {
		return codec;
	}
	
	public void setCodec(String codec) {
		this.codec = codec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Station [name=" + name + ", id=" + id + "]";
	}

}
