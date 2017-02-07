package bo.radio.tuner.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SerializeObjectUtils<T> {
	private final File destinationFile;
	private final Class<T> classType;

	public SerializeObjectUtils(File destinationFile, Class<T> classType) {
		this.destinationFile = destinationFile;
		this.classType = classType;
	}

	public void write(T t) throws FileNotFoundException, IOException {
		ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(destinationFile));
		try {
			oo.writeObject(t);
		} finally {
			oo.close();
		}
	}

	public T read() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(destinationFile));
		try {
			return classType.cast(ois.readObject());
		} finally {
			ois.close();
		}
	}

}
