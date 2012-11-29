package clusterer;

/**
 * Creates a new {@code IAttribute} object based on
 * a single rating or other IAttribute objects.
 */
import java.util.List;

public abstract class AttributeFactory {
	
	/**
	 * Creates a new {@code IAttribute} object based on a single rating.
	 * 
	 * @param rating the rating for the new {@code IAttribute} object.
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createAttribute(double rating); // single node
	
	/**
	 * Creates a new {@code IAttribute} object based on a list of other {@code IAttribute}
	 * objects.
	 * 
	 * @param attributes list of {@code IAttribute} objects as
	 * input for new {@code IAttribute} objects.
	 * 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createAttribute(List<IAttribute> attributes); // group node
	

}
