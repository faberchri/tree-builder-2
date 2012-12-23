package client;

/**
 * 
 * Normalizes a rating as it appeared in the data set,
 * which stores ratings as integers.
 *
 */
public class IntegerNormalizer implements INormalizer<Integer> {
	
	/**
	 * The minimal rating of the data set.
	 */
	private final int minValue;
	
	/**
	 * The maximal rating of the data set.
	 */
	private final int maxValue;
	
	/**
	 * The range of the ratings: ({@code maxValue - minValue}).
	 */
	private double range;
	
	/**
	 * Instantiates a new normalizer for integer values.
	 * 
	 * @param minValue the minimal rating in the data set.
	 * @param maxValue the maximal rating in the data set.
	 */
	public IntegerNormalizer(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		range = (double)(maxValue - minValue);
	}
	
	@Override
	public double normalizeRating(Integer rating) {
		return ((rating.doubleValue() - (double)minValue) / range) * 10.0;
	}

}
