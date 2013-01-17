package ch.uzh.agglorecommender.client.jcommander;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;


/**
 * Checks write access to passed path.
 * Application exits if no write access is granted.
 * @param path to check access.
 */
public class FileWriteValidator implements IParameterValidator {
	@Override
	public void validate(String name, String path)
			throws ParameterException {
		if (path == null) return;
		File file = new File(path);
		String message = "The specified path " + path + " is not a valid write location due " +
				"to one of the following reasons: The file exists but is a directory "+
				"rather than a regular file or does not exist but cannot " +
				"be created, or cannot be opened for any other reason.";
		if (! file.exists()) {
			try {
				new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				throw new ParameterException(message);
			}
			file.delete();
		} else {
			if (! file.canWrite()) {
				throw new ParameterException(message);
			}
		}
	}		
}
