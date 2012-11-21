package client;

/**
 * 
 * Normalizes ratings stored in the data set
 * to rating values between 0.0 and 1.0.
 *
 * @param <T> The data type of raw ratings in the data set.
 */
public interface Normalizer<T> {
	
	/**
	 * Normalizes a rating.
	 * 
	 * @param rating The rating as it appeared in the data set.
	 * @return the normalized rating: [0.0, 1.0].
	 */
	public double normalizeRating(T rating);
}
