package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.util.Iterator;
import java.util.Map.Entry;


/**
 * The attribute value for a particular attribute key.
 * <br><b>
 * Implementations store the attribute values
 * for a particular key in the attribute map of a node.
 */
public interface IAttribute {
			
	/**
	 * Gets the number of other attributes objects (i.e. ratings)
	 * on which this attribute object is based.
	 * Is 1 for a ClassitAttribute of a leaf node.
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
	 * <br>
	 * <br>
	 * Used for CLASSIT.
	 * @return the support value of this attribute.
	 */
	public int getSupport() throws UnsupportedOperationException;
	
	/**
	 * Gets the sum of all squared ratings of children with the same attribute key.
	 * 
	 * <pre>
	 * Example:             S:24
	 *                      S^2:174
	 *                      Sup:4
	 *                      /   \
	 *                    /       \
	 *                  /           \
	 *           S:7                S:17
	 *           S^2:29             S^2:145 
	 *           Sup:2              Sup:2 
	 *           /   \              /    \
	 *          /     \            /      \
	 *      S:2      S:5        S:8       S:9
	 *      S^2:4    S^2:25     S^2:64    S^2:81
	 *      Sup:1    Sup:1      Sup:1     Sup:1
	 * </pre>
	 * <br>
	 * <br>
	 * Used for CLASSIT.
	 * @return the sum of all squared rating values of the attribute.
	 * @throws UnsupportedOperationException for CobwebAttribute
	 */
	public double getSumOfSquaredRatings() throws UnsupportedOperationException;

	/**
	 * Gets the sum of all ratings of children with the same attribute key.
	 * 	 * <pre>
	 * Example:             S:24
	 *                      S^2:174
	 *                      Sup:4
	 *                      /   \
	 *                    /       \
	 *                  /           \
	 *           S:7                S:17
	 *           S^2:29             S^2:145 
	 *           Sup:2              Sup:2 
	 *           /   \              /    \
	 *          /     \            /      \
	 *      S:2      S:5        S:8       S:9
	 *      S^2:4    S^2:25     S^2:64    S^2:81
	 *      Sup:1    Sup:1      Sup:1     Sup:1
	 * </pre>
	 * <br>
	 * <br>
	 * Used for CLASSIT.
	 * @return the sum of all rating values of the attribute.
	 * @throws UnsupportedOperationException for CobwebAttribute
	 */
	public double getSumOfRatings() throws UnsupportedOperationException;
	
	
	/**
	 * Gets an iterator over the attributes probability map.
	 * <br>
	 * e.g.:
	 * <br>
	 * <pre>
	 *   Rating Values  |    Probability
	 * -----------------------------------
	 *       5          |       0.5
	 *       1          |       0.25
	 *       
	 * Probabilities do not necessarily add
	 * up to 1, due to the sparse nature of
	 * the user-content-rating matrix.
	 * </pre>
	 * <br>
	 * <br>
	 * Used for COBWEB.
	 * @return an iterator over all probability map entries.
	 * @throws UnsupportedOperationException for ClassitAttributes
	 */
	public Iterator<Entry<Object, Double>> getProbabilities() throws UnsupportedOperationException;
		
}
