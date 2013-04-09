package ch.uzh.agglorecommender.client.jcommander;

import ch.uzh.agglorecommender.recommender.clients.DefaultClient;
import ch.uzh.agglorecommender.recommender.clients.IClient;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class ClientValidatorConverter implements IParameterValidator, IStringConverter<IClient>{

	private static Class<IClient> clazz = null;
	
	@Override
	public IClient convert(String simpleClassName) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParameterException("The specified client " + clazz.getName() + " could not be instantiated.");  
		}
	}

	@Override
	public void validate(String name, String simpleClassName) throws ParameterException {
		String fqcp = DefaultClient.class.getName();
		fqcp = fqcp.substring(0,(fqcp.lastIndexOf(".") + 1)).concat(simpleClassName);
		try {
			clazz = (Class<IClient>) Class.forName(fqcp).asSubclass(IClient.class);
			
		} catch (ClassNotFoundException  | ClassCastException e) {
			throw new ParameterException("The specified client " + fqcp + " is not known.");  
		}
	}
}
