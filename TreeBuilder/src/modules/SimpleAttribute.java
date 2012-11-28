package modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clusterer.IAttribute;

/**
 * 
 * A simple attribute object, which stores average, standard deviation
 * and support of a particular key in the attribute map of a node.
 * <br>
 * Is IMMUTABLE!
 *
 */
class SimpleAttribute implements IAttribute {
//	private static Set<Double> allRatings;
	
	/**
	 * Average of this attribute.
	 */
	private final double average;
	
	/**
	 * Standard deviation of this attribute.
	 */
	private final double stdDev;
	
	/**
	 * Support of this attribute
	 */
	private final int support;
	
	/**
	 * Ratings used to calculate average and standard deviation.
	 */
	private final List<Double> consideredRatings;
//	private final double[] consideredRatings; //FIXME is mutable: we would need something like: List<Integer> items = Collections.unmodifiableList(Arrays.asList(0,1,2,3));
	
	/**
	 * Instantiates a new {@code SimpleAttribute} object based on a
	 * single rating and not a combination of other attribute objects.
	 * Sets support to 1 and standard deviation to 0.0.
	 * @param rating the rating for the attribute
	 */
	public SimpleAttribute(double rating) {
		this.average = rating;
		this.stdDev = 0.0;
		this.support = 1;
//		allRatings.add(average);
		List<Double> li = new ArrayList<Double>();
		li.add(average); //new double[] {average};
		consideredRatings= Collections.unmodifiableList(li); // ensures immutability of list
	}
	
	/**
	 * Instantiates a new {@code SimpleAttribute} object
	 * based on multiple ratings
	 * obtained from two or more other attribute objects.
	 * @see a sublass of AttributeFactory for details
	 * of the parameter calculation.
	 * 
	 * @param average the average of this attribute object
	 * @param stdDev the standard deviation of this attribute object
	 * @param support the support of this attribute object
	 * @param consideredRatings the list of all underlying
	 * ratings used to calculate the parameters of this attribute object.
	 */
	public SimpleAttribute(double average, double stdDev,
			int support, List<Double> consideredRatings) {
		this.average = average;
		this.stdDev = stdDev;
		this.support = support;
		this.consideredRatings = consideredRatings;
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
	
	public List<Double> getConsideredRatings() {
		return consideredRatings;
	}
	
	@Override
	public String toString() {
		return "Avg.: "
				.concat(String.valueOf(getAverage()))
				.concat(", StdDev.: ")
				.concat(String.valueOf(getStdDev()))
				.concat(", Sup.: ")
				.concat(String.valueOf(getSupport()));
	}
}
