package bo.radio.tuner.model;

public class Station {
	// Technical Info
	private String stream;
	private int id;
	
	// Human readable Info
	private String name;
	private float bitRate;
	private float sampleRate;
	private Category category;

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
