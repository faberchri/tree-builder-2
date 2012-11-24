package clusterer;

/**
 * Creates a new {@code IAttribute} object based on
 * a single rating or other IAttribute objects.
 */
import java.util.List;

abstract class AttributeFactory {
	
	public abstract IAttribute createAttribute(double rating); // single node
	
	public abstract IAttribute createAttribute(List<IAttribute> attributes); // group node
	

}
