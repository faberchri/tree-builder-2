package ch.uzh.agglorecommender.client.jcommander;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.SharedTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * Checks if the specified algo is known and references the corresponding TreeCompponentFactory.
 *
 */
public class AlgoConverterValidator implements IStringConverter<TreeComponentFactory>, IParameterValidator {
	@Override
	public TreeComponentFactory convert(String algo) {
		if (algo.equalsIgnoreCase("classit")) {
			return ClassitTreeComponentFactory.getInstance();
		}
		if (algo.equalsIgnoreCase("cobweb")) {
			return CobwebTreeComponentFactory.getInstance();
		}
		if (algo.equalsIgnoreCase("shared")) {
			return SharedTreeComponentFactory.getInstance();
		}
		return null;
	}

	@Override
	public void validate(String name, String value)
			throws ParameterException {
		if (! value.equalsIgnoreCase("classit") && ! value.equalsIgnoreCase("cobweb") && ! value.equalsIgnoreCase("shared")) {
			throw new ParameterException("Parameter " + name + " should be \"cobweb\" or \"classit\" (found " + value +")");
		}
	}

}
