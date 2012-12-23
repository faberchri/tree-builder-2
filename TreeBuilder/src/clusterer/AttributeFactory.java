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
	 * Creates a new {@code IAttribute} object for the specified attribute
	 * (e.g. User_A) based on the list of nodes to merge
	 * (e.g. Movie_1, Movie_2, Movie_3).
	 *
	 * 
	 * @param nodesToMerge list of {@code INode} objects that are merged.
	 * @param attributeKey the attribute key of the new attribute object.
	 * 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createAttribute(INode attributeKey, List<INode> nodesToMerge); // group node
	

}
