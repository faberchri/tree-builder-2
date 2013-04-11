package ch.uzh.agglorecommender.client.jcommander;

import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.clusterer.treeupdate.SimpleNodeUpdater;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;


/**
 * Checks if the specified node updater name is a class name of an INodeUpdater
 * implementation. Instantiates a new object of the class if true. 
 */
public class NodeUpdaterValidatorConverter implements IParameterValidator, IStringConverter<INodeUpdater> {
	
	private static Class<INodeUpdater> clazz = null;

	@Override
	public INodeUpdater convert(String simpleClassName) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParameterException("The specified node updater " + clazz.getName() + " could not be instantiated.");  
		}
	}

	@Override
	public void validate(String name, String simpleClassName)
			throws ParameterException {
		String fqcp = SimpleNodeUpdater.class.getName();
		fqcp = fqcp.substring(0,(fqcp.lastIndexOf(".") + 1)).concat(simpleClassName);
		try {
			clazz = (Class<INodeUpdater>) Class.forName(fqcp).asSubclass(INodeUpdater.class);
			
		} catch (ClassNotFoundException  | ClassCastException e) {
			throw new ParameterException("The specified node updater " + fqcp + " is not known.");  
		}
		
	}
	
}
