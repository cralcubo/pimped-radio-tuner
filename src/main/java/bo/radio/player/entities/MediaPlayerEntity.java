package bo.radio.player.entities;

import java.io.Serializable;

public class MediaPlayerEntity implements Serializable {
	private static final long serialVersionUID = -8659488002797849644L;
	private final int volumeLevel;
	
	public MediaPlayerEntity(int level) {
		this.volumeLevel = level;
	}
	
	public int getLevel() {
		return volumeLevel;
	}

	@Override
	public String toString() {
		return "MediaPlayerEntity [volumeLevel=" + volumeLevel + "]";
	}

}
