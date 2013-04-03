package ch.uzh.agglorecommender.client.jcommander;

import ch.uzh.agglorecommender.client.CommandLineArgs;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Checks if property files value is either string "random" or a readable file.
 *
 */
public class DatasetValidator extends FileReadValidatorConverter implements IParameterValidator {

	private static boolean random = false;
	
	@Override
	public void validate(String name, String pathOrRandom) throws ParameterException {
		if (random || pathOrRandom.equalsIgnoreCase(CommandLineArgs.randomDatasetIdentifier)) {
			random = true;
		} else {
			super.validate(name, pathOrRandom);
		}		
	}
}
