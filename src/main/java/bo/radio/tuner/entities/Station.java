package bo.radio.tuner.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stations")
public class Station {
	public static final String IDCOLUMN_NAME = "id";
	
	@DatabaseField(columnName = IDCOLUMN_NAME, generatedId = true)
	private int id;
	
	@DatabaseField
	private String stream;
	@DatabaseField
	private String name;
	@DatabaseField
	private float bitRate;
	@DatabaseField
	private float sampleRate;
	
	public Station() {
		// empty for ORM lite
	}
	
	public Station(String name, String stream) {
		this(new Builder(name, stream));
	}
	
	private Station(Builder b) {
		stream = b.stream;
		name = b.name;
		bitRate = b.bitRate;
		sampleRate = b.sampleRate;
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
		Station other = (Station) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", stream=" + stream + ", name=" + name + ", bitRate=" + bitRate + ", sampleRate="
				+ sampleRate + "]";
	}

	public static class Builder {
		private String stream = "";
		private String name = "";
		private float bitRate = 0f;
		private float sampleRate = 0f;
		
		public Builder(String name, String stream) {
			this.name = name;
			this.stream = stream;
		}
		
		public Builder bitRate(float val) {
			bitRate = val;
			return this;
		}
		
		public Builder sampleRate(float val) {
			sampleRate = val;
			return this;
		}
		
		public Station build() {
			return new Station(this);
		}
	}

}
