package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * 
 * A simple parameter object, which stores sum of ratings, sum of squared ratings
 * and support of a particular key in the attribute map of a node.
 * <br>
 * Is immutable.
 *
 */
final class ClassitAttribute implements IAttribute, Serializable {
	
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
	public ClassitAttribute(int support, double sumOfRatings,
			double sumOfSquaredRatings) {

		this.support = support;
		this.sumOfRatings = sumOfRatings;
		this.sumOfSquaredRatings = sumOfSquaredRatings;
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
		throw new UnsupportedOperationException(
				"Method of CobwebAttribute object called on ClassitAttribute object");
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
	
}
