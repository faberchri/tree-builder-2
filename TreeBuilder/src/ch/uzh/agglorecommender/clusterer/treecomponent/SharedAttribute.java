package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
 * A simple parameter object, which stores sum of ratings, sum of squared ratings
 * and support of a particular key in the attribute map of a node.
 * <br>
 * Is immutable.
 *
 */
public final class SharedAttribute implements IAttribute, Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Support of this attribute.
	 * 
	 */
	private final int support;
		
	private final double sumOfRatings;
	
	private final double sumOfSquaredRatings;
	
	private final Map<String,String> meta;
	
	/**
	 * Attribute value - probability pairs.
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
	 */
	private final Map<Object, Double> attributeProbabilities;
				
	/**
	 * Instantiates a new {@code ClassitAttribute}
	 * with stdDev, average, support, sum of ratings, and sum of squared ratings.
	 * <br>
	 * Creates an attribute object that can be used for CLASSIT.
	 * 
	 * @param sumOfRatings the sum of all ratings
	 * @param sumOfSquaredRatings the sum of all squared ratings
	 * @param support the support of this attribute object.
	 */
	public SharedAttribute(int support, double sumOfRatings,
			double sumOfSquaredRatings, Map<?, Double> probabilityMap, Map<String,String> meta) {

		this.support = support;
		this.sumOfRatings = sumOfRatings;
		this.sumOfSquaredRatings = sumOfSquaredRatings;
		this.attributeProbabilities = (Map<Object, Double>) probabilityMap;
		this.meta = meta;
	}
	
	public int getSupport() {
		return support;
	}
		
	@Override
	public double getSumOfRatings() {
		return sumOfRatings;
	}
	
	@Override
	public double getSumOfSquaredRatings() {
		return sumOfSquaredRatings;
	}

	@Override
	public Iterator<Entry<Object, Double>> getProbabilities() {
		return attributeProbabilities.entrySet().iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("support: ");
		sb.append(String.valueOf(getSupport()));
		sb.append(", sum of ratings: ");
		sb.append(String.valueOf(getSumOfRatings()));
		sb.append(", sum of squared ratings.: ");
		sb.append(String.valueOf(getSumOfSquaredRatings()));
		return sb.toString();
	}

	@Override
	public Map<String,String> getMeta() {
		return meta;
	}

	@Override
	public double getMeanOfRatings() {
		return sumOfRatings / support;
	}
}
