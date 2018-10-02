package bo.radio.player;

import java.util.Optional;

import bo.radio.player.entities.MediaPlayerEntity;

/**
 * Class that will contain all the methods that will be concerned on saving the
 * state of pimp-radio media player.
 * 
 * @author christian
 *
 */
public interface MediaPlayerDaoApi {

	/**
	 * Save the state of a media player entity
	 */
	void saveMediaPlayerState(MediaPlayerEntity mediaPlayerEntity);

	/**
	 * Gets the saved state of a media player entity.
	 * <p/>
	 * If the entity is not found or there is an error, an empty optional is
	 * returned.
	 */
	Optional<MediaPlayerEntity> getMediaPlayerState();
}
