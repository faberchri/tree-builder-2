package clusterer;

import java.util.List;


/**
 * Interface to get the parameter values (average, standard deviation, and support)
 * for a particular key in the attribute map of a node.
 * <br>
 * Please implement IMMUTABLE!
 *
 */
public interface IAttribute {
	
	/**
	 * Gets the average of this attribute
	 * object based on all underlying ratings.
	 * <pre>
	 * Example:             Av:0.45
	 *                      Std:0.3
	 *                      Sup:4
	 *                      /   \
	 *                    /       \
	 *                  /           \
	 *           Av:0.4             Av:0.5
	 *           Std:0.28           Std:0.42 
	 *           Sup:2              Sup:2 
	 *           /   \              /    \
	 *          /     \            /      \
	 *      Av:0.2   Av:0.6     Av:0.8    Av:0.2
	 *      Std:0.0  Std:0.0    Std:0.0   Std:0.0
	 *      Sup:1    Sup:1      Sup:1     Sup:1
	 * </pre>
	 * 
	 * @return the average of this attribute.
	 */
	public double getAverage();
	
	/**
	 * Gets the standard deviation of this attribute
	 * object based on all underlying ratings.
	 * Is 0 for an Attribute of a leaf node. 
	 * <pre>
	 * Example:             Av:0.45
	 *                      Std:0.3
	 *                      Sup:4
	 *                      /   \
	 *                    /       \
	 *                  /           \
	 *           Av:0.4             Av:0.5
	 *           Std:0.28           Std:0.42 
	 *           Sup:2              Sup:2 
	 *           /   \              /    \
	 *          /     \            /      \
	 *      Av:0.2   Av:0.6     Av:0.8    Av:0.2
	 *      Std:0.0  Std:0.0    Std:0.0   Std:0.0
	 *      Sup:1    Sup:1      Sup:1     Sup:1
	 * </pre>
	 * 
	 * @return the standard deviation of this attribute.
	 */
	public double getStdDev();
	
	/**
	 * Gets the number of other attributes objects (i.e. ratings)
	 * on which this attribute object is based.
	 * Is 1 for an Attribute of a leaf node.
	 * 	 * <pre>
	 * Example:             Av:0.45
	 *                      Std:0.3
	 *                      Sup:4
	 *                      /   \
	 *                    /       \
	 *                  /           \
	 *           Av:0.4             Av:0.5
	 *           Std:0.28           Std:0.42 
	 *           Sup:2              Sup:2 
	 *           /   \              /    \
	 *          /     \            /      \
	 *      Av:0.2   Av:0.6     Av:0.8    Av:0.2
	 *      Std:0.0  Std:0.0    Std:0.0   Std:0.0
	 *      Sup:1    Sup:1      Sup:1     Sup:1
	 * </pre>
	 * 
	 * @return the support value of this attribute.
	 */
	public int getSupport();
	
	/**
	 * Gets the list of ratings used for
	 * calculating the attribute parameters.
	 * 
	 * @return an IMMUTABLE list of all the underlying
	 * rating values of this attribute.
	 */
	public List<Double> getConsideredRatings();
}
