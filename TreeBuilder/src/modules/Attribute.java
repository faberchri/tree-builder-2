package modules;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import clusterer.IAttribute;

/**
 * 
 * A simple attribute object, which stores average, standard deviation
 * and support of a particular key in the attribute map of a node.
 * <br>
 * Is IMMUTABLE!
 *
 */
class Attribute implements IAttribute, Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Average of this attribute.
	 * <br>
	 * Used for CLASSIT.
	 */
	private final double average;
	
	/**
	 * Standard deviation of this attribute.
	 * <br>
	 * Used for CLASSIT.
	 */
	private final double stdDev;
	
	/**
	 * Support of this attribute.
	 * <br>
	 * NEITHER USED FOR COBWEB NOR CLASSIT.
	 */
	private final int support;
	
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
	 * <br>
	 * <br>
	 * Used for COBWEB.
	 */
	private final Map<Object, Double> attributeProbabilities;
	
	private final double sumOfRatings;
	
	private final double sumOfSquaredRatings;
		
	/**
	 * Instantiates a new {@code Attribute} with a 
	 * attribute-value probability map.
	 * <br>
	 * average, stdev, support are initialized to -1.
	 * <br>
	 * Creates an attribute object that can be used for COBWEB.
	 * @param attribute-value probability map of this attribute.
	 */
	public Attribute(Map<?, Double> probabilityMap) {
		this.attributeProbabilities = (Map<Object, Double>) probabilityMap;
		
		this.average = -1.0;
		this.stdDev = -1.0;
		this.support = -1;
		
		this.sumOfRatings = -1.0;
		this.sumOfSquaredRatings = -1.0;
	}
		
	/**
	 * Instantiates a new {@code Attribute}
	 * with stdDev, average, support, sum of ratings, and sum of squared ratings.
	 * <br>
	 * Creates an attribute object that can be used for CLASSIT.
	 * 
	 * @param sumOfRatings the sum of all ratings
	 * @param sumOfSquaredRatings the sum of all squared ratings
	 * @param support the support of this attribute object.
	 * @param average average value of attribute NOT USED
	 * @param stdDev standard deviation of attribute NOT USED
	 */
	public Attribute(double average, double stdDev, int support, double sumOfRatings,
			double sumOfSquaredRatings) {
		this.average = average;
		this.stdDev = stdDev;
		this.support = support;
		this.sumOfRatings = sumOfRatings;
		this.sumOfSquaredRatings = sumOfSquaredRatings;
		
		this.attributeProbabilities = null;
	}

	public double getAverage() {
		return average;
	}
	
	public double getStdDev() {
		return stdDev;
	}
	
	public int getSupport() {
		return support;
	}
	
	public Iterator<Entry<Object, Double>> getProbabilities() {
		return attributeProbabilities.entrySet().iterator();
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Avg.: ");
		sb.append(String.valueOf(getAverage()));
		sb.append(", StdDev.: ");
		sb.append(String.valueOf(getStdDev()));
		sb.append(", Sup.: ");
		sb.append(String.valueOf(getSupport()));
		sb.append(", attribute probabilities: ");
		sb.append(attributeProbabilities);
		return sb.toString();
	}
}
