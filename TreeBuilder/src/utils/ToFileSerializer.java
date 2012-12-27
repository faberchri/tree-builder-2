package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.logging.Logger;

import clusterer.TreeBuilder;

public class ToFileSerializer {
	
	/**
	 * Defines the serialization frequency. In minutes.
	 * Please, adapt to your needs.
	 */
	private static final int serializationTimeInterval = 10; // in minutes
	
	private static Logger log = TBLogger.getLogger(ToFileSerializer.class.toString());
	
	private static long lastSerialization = System.currentTimeMillis();

	/**
	 * Serializes the passed TreeBuilder to the specified file, given that 
	 * the last serialization did take place before (current time - serializationTimeInterval).
	 * 
	 * @param objectToSerialize The TreeBuilder to serialize.
	 * @param pathToFile The location where the serialized file is written.
	 * @param builderId The id of the TreeBuilder.
	 * @throws FileNotFoundException if the file exists but is a directory 
	 * rather than a regular file, does not exist but cannot be created, 
	 * or cannot be opened for any other reason 
	 */
	public static void serialize(TreeBuilder objectToSerialize, String pathToFile, UUID builderId) {
		// return if no serialization path was specified
		if (pathToFile == null) return;
		
		// return if serialization is not yet due.
		if (((double)(System.currentTimeMillis() - lastSerialization)) / 1000.0 < serializationTimeInterval * 60.0) {
			log.info("Not yet time for serialization: "+ ((double)(System.currentTimeMillis() - lastSerialization) / 1000.0 ) 
					+ " < " + serializationTimeInterval * 60.0);
			return;
		}
		
		// do serialization
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		StringBuilder sb = new StringBuilder();
		sb.append(pathToFile);
		if (pathToFile.endsWith(".ser")) {
			sb.delete(sb.length() - 4, sb.length());
		}
		if (! pathToFile.endsWith(File.separator)) {
			sb.append("_");
		}
		sb.append("TreeBuilder_");
		sb.append(builderId.toString());
		pathToFile = sb.append(".ser").toString();
		// use previous serialization step as back up if next serialization fails.
		File oldSer = new File(pathToFile);
		if (oldSer.exists()) {			
			oldSer.renameTo(new File(pathToFile.concat(".backup")));
		}
		try {
			log.warning("Serialization started ... Don't terminate application!");
			fos = new FileOutputStream(pathToFile);
			out = new ObjectOutputStream(fos);
			out.writeObject(objectToSerialize);
			log.warning("Serialization completed. Now you can terminate.");
			lastSerialization = System.currentTimeMillis();
			log.info("TreeBuilder serialized to file " + pathToFile);
			out.close();
		} catch (IOException e) {
			log.severe(e.getStackTrace().toString());
			log.severe("IOException on writing .ser file: " + pathToFile);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * De-serializes a TreeBuilder from the specified file.
	 * 
	 * @param pathToFile the file from which the TreeBuilder is de-serialized.
	 * @return a de-serialized TreeBuilder or null if path is not readable or 
	 * de-serialization failed.
	 */
	public static TreeBuilder deserialize(String pathToFile) {
		File file  = new File(pathToFile);
		if (! file.exists() || ! file.canRead()) return null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		TreeBuilder tb = null;
		try {
			fis = new FileInputStream(pathToFile);
			in = new ObjectInputStream(fis);
			tb = (TreeBuilder) in.readObject();		
			log.info("TreeBuilder de-serialized from file " + pathToFile);
			in.close();
		} catch (ClassNotFoundException | IOException e) {
			log.severe(e.getStackTrace().toString());
			log.severe("Exception on reading .ser file: " + pathToFile);
			log.severe("This error is most likely caused by an attempt to read a .ser file " +
					"that resulted from an incompleted previous serialization process." +
					" Try to restart from .ser.backup file.");
			e.printStackTrace();
			System.exit(-1);
		}
		return tb;
	}
}


