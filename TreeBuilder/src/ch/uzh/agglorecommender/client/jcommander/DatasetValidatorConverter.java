package ch.uzh.agglorecommender.client.jcommander;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.client.RandomDataset;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * Checks if the specified data set type is known and references the corresponding class.
 *
 */
public class DatasetValidatorConverter implements IParameterValidator, IStringConverter<Class<IDataset<?>>> {

	private static Class<IDataset<?>> clazz = null;
	
	@Override
	public void validate(String name, String dataset)
			throws ParameterException {
		
		// first char of dataset to uppercase
		char[] stringArray = dataset.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		dataset = new String(stringArray);
		
		// create fully qualified class path
		String fqcp = RandomDataset.class.getName();
		fqcp = fqcp.substring(0,(fqcp.lastIndexOf(".") + 1)).concat(dataset).concat("Dataset");
		
		// look up string
		try {
			clazz = (Class<IDataset<?>>) Class.forName(fqcp).asSubclass(IDataset.class);				
		} catch (ClassNotFoundException  | ClassCastException e) {
			throw new ParameterException("The specified data set type " + fqcp + " is not known.");  
		}
		
	}

	@Override
	public Class<IDataset<?>> convert(String simpleName) {
//		System.out.println("converter: " + clazz.toString());
		return clazz;
	}				
}