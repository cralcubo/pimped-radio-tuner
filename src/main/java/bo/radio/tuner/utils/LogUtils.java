package bo.radio.tuner.utils;

import java.util.function.Supplier;

import org.slf4j.Logger;

public class LogUtils {
	
	public static void logDebug(Logger logger, Supplier<String> message) {
		if(logger.isDebugEnabled()) {
			logger.debug(message.get());
		}
	}

}
