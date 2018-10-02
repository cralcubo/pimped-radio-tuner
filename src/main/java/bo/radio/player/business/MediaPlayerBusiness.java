package bo.radio.player.business;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bo.radio.player.MediaPlayerDaoApi;
import bo.radio.player.entities.MediaPlayerEntity;
import bo.radio.tuner.utils.SerializeObjectUtils;

/**
 * Class that will save the pimp-radio media player state in the file system.
 * 
 * @author christian
 *
 */
public class MediaPlayerBusiness implements MediaPlayerDaoApi {
	private final static Logger log = LoggerFactory.getLogger(MediaPlayerBusiness.class);
	private static final String MEDIAPLAYERPATH_KEY = "mediaplayer.path";

	private final SerializeObjectUtils<MediaPlayerEntity> mediaPlayerSerializer;

	public MediaPlayerBusiness(Properties databaseProperties) {
		String serializedMediaPath = databaseProperties.getProperty(MEDIAPLAYERPATH_KEY);
		File destinationFile = new File(serializedMediaPath);
		if (!destinationFile.exists()) {
			try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("There was an error creating the File:" + serializedMediaPath
						+ ". Make sure that there is enough rights to create a file in that folder.");
			}
		}
		mediaPlayerSerializer = new SerializeObjectUtils<>(destinationFile, MediaPlayerEntity.class);
	}

	@Override
	public void saveMediaPlayerState(MediaPlayerEntity entity) {
		log.info("Saving: {}", entity);
		try {
			mediaPlayerSerializer.write(entity);
		} catch (IOException e) {
			log.error("There was an error trying to save: {}", entity, e);
		}
	}

	@Override
	public Optional<MediaPlayerEntity> getMediaPlayerState() {
		 try {
			return Optional.ofNullable(mediaPlayerSerializer.read());
		} catch (ClassNotFoundException | IOException e) {
			log.error("There was an error reading the save media player entity.", e);
			return Optional.empty();
		}
	}

}
