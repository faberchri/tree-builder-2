package ch.uzh.agglorecommender.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Logger;



public class ToFileSerializer {
	
	/**
	 * Defines the serialization frequency. In minutes.
	 * Please, adapt to your needs.
	 */
	private static final int serializationTimeInterval = 10; // in minutes
	
	private static Logger log = TBLogger.getLogger(ToFileSerializer.class.toString());
	
	private static long lastSerialization = System.currentTimeMillis();

	/**
	 * Serializes the passed Object to the specified file, given that 
	 * the last serialization did take place before (current time - serializationTimeInterval).
	 * 
	 * @param objectToSerialize The object (i.e. TreeBuilder) to serialize.
	 * @param pathToFile The location where the serialized file is written.
	 * @param builderId The id of the TreeBuilder. Is appended to the filename.
	 *
	 */
	public static void serializeConditionally(Serializable objectToSerialize, String pathToFile, UUID builderId) {
		
		// return if no serialization path was specified
		if (pathToFile == null) return;
		
		// return if serialization is not yet due.
		if (((double)(System.currentTimeMillis() - lastSerialization)) / 1000.0 < serializationTimeInterval * 60.0) {
			log.info("Not yet time for serialization: "+ ((double)(System.currentTimeMillis() - lastSerialization) / 1000.0 ) 
					+ " < " + serializationTimeInterval * 60.0);
			return;
		}
		serialize(objectToSerialize, pathToFile, builderId);
	}
	
	/**
	 * Serializes the passed Object to the specified file.
	 * 
	 * @param objectToSerialize object (i.e. TreeBuilder) to serialize.
	 * @param pathToFile The location where the serialized file is written.
	 * @param builderId The id of the TreeBuilder. Is appended to the filename.
	 */
	public static void serialize(Serializable objectToSerialize, String pathToFile, UUID objectUuid) {
		// return if no serialization path was specified
		if (pathToFile == null) return;
		
		// ok we serialize ... 
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		// create output file path
		pathToFile = createOutputFilePath(objectToSerialize, pathToFile, objectUuid);
	
		File ser = new File(pathToFile);
		
		// create missing directories if necessary
		if (! ser.getParentFile().exists()) {
			ser.mkdirs();
		}
		
		// use previous serialization step as backup if next serialization fails.
		if (ser.exists()) {			
			ser.renameTo(new File(pathToFile.concat(".backup")));
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
	 * De-serializes an object from the specified file.
	 * 
	 * @param pathToFile the file from which the TreeBuilder is de-serialized.
	 * @return a de-serialized object or null if path is not readable or 
	 * de-serialization failed.
	 */
	public static Object deserialize(String pathToFile) {
		File file  = new File(pathToFile);
		if (! file.exists() || ! file.canRead()) return null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object o = null;
		try {
			fis = new FileInputStream(pathToFile);
			in = new ObjectInputStream(fis);
			o = in.readObject();		
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
		return o;
	}
	
	/**
	 * Creates the path to the output file according to the following schema:
	 * <br>
	 * {@code <pathToFile-parent-directory>/<pathToFile-last-component-minus-possible-".ser">_<simple-class-name-of-objectToSerialize>_<objectId.toString()>.ser}
	 * @param objectToSerialize the object that is serialized
	 * @param pathToFile the output path as specified by the users input argument
	 * @param objectId the uuid of the object to serialize
	 * @return the path to the output file
	 */
	private static String createOutputFilePath(Object objectToSerialize, String pathToFile, UUID objectId) {
		if (pathToFile == null) return null;
		
		// create default string
		StringBuilder dsb = new StringBuilder(50);
		dsb.append(objectToSerialize.getClass().getSimpleName());
		dsb.append("_");
		dsb.append(objectId.toString());
		dsb.append(".ser");
		String defaultString = dsb.toString();
		
		File outF = new File(pathToFile);
		StringBuilder sb = new StringBuilder();
		String customName = "";
		if (! outF.isDirectory()) {
			customName = outF.getName();
			outF = outF.getParentFile();
		}
		
		if (! customName.endsWith(defaultString)) {
			if (customName.endsWith(".ser")) {
				sb.append(customName.substring(0, customName.length() - 4));
			} else {
				sb.append(customName);
			}
			if (sb.length() != 0) {
				sb.append("_");
			}
			sb.append(defaultString);
		} else {
			sb.append(customName);
		}
		
		// combine parent file with new file name to obtain path
		outF = new File(outF.getPath(), sb.toString());
		return outF.getPath();
	}
}


