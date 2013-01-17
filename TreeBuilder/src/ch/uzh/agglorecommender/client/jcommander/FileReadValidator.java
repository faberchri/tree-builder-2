package ch.uzh.agglorecommender.client.jcommander;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;


/**
 * Checks read access to passed path.
 * Application exits if no read access is granted.
 * @param path to check access.
 */
public class FileReadValidator implements IParameterValidator {		
	@Override
	public void validate(String name, String path)
			throws ParameterException {
		if (path == null) return;
		try {
			FileInputStream fis = new FileInputStream(path);
			fis.close();
		} catch (FileNotFoundException e) {
			throw new ParameterException(
					"The specified path " + path + " is not a valid read location due " +
					"to one of the following reasons: The file does not exist, "+
					"is a directory rather than a regular file, "+
					"or for some other reason cannot be opened for reading.");
		} catch (IOException e) {
			throw new ParameterException("The specified path " + path + " could not be accessed due to IOException");
		}			
	}		
}