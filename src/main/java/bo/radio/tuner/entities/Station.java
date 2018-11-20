package bo.radio.tuner.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stations")
public class Station extends TunerEntity implements Serializable {
	private static final long serialVersionUID = 5420799713581070156L;
	
	public static final String NAMECOLUMN_NAME = "name";
	public static final String STREAMCOLUMN_NAME = "stream";
	
	@DatabaseField(columnName = STREAMCOLUMN_NAME)
	private String stream;
	@DatabaseField(columnName = NAMECOLUMN_NAME)
	private String name;
	@DatabaseField
	private float bitRate;
	@DatabaseField
	private float sampleRate;
	@DatabaseField
	private String codec;

	transient private List<Category> categories = new ArrayList<>();

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
	public String toString() {
		return "Station [stream=" + stream + ", name=" + name + ", sampleRate=" + sampleRate + "]";
	}

}
