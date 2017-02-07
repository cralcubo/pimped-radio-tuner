package bo.radio.tuner.dao.utils;

import java.io.File;

public class FileUtils {
	
	public static void removeFolderContents(File file) {
		deleteDirectory(file, false);
	}

	private static void deleteDirectory(File file, boolean deleteRoot) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteDirectory(f, true);
			}
			if (deleteRoot) {
				file.delete();
			}
		} else {
			// this is just a file
			file.delete();
		}
	}

}
