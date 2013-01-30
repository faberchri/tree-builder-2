package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.util.Iterator;
import java.util.Map.Entry;



/**
 * Implementations shall store the the attribute values
 * for a particular key in the attribute map of a node.
 *
 */
public interface IAttribute {
			
	/**
	 * Gets the number of other attributes objects (i.e. ratings)
	 * on which this attribute object is based.
	 * Is 1 for an ClassitAttribute of a leaf node.
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
	
	public double getSumOfSquaredRatings() throws UnsupportedOperationException;

	public double getSumOfRatings() throws UnsupportedOperationException;
	
//	public double getStd() throws UnsupportedOperationException;
	
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
	 */
	public Iterator<Entry<Object, Double>> getProbabilities() throws UnsupportedOperationException;

	/**
	 * Get the clustering method used in this attribute (classit/cobweb/..)
	 *
	 */
	public String getClusteringMethod();
}
