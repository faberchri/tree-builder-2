package ch.uzh.agglorecommender.client;

import java.io.Serializable;


/**
 * 
 * Normalizes ratings stored in the data set
 * to rating values between 0.0 and 1.0.
 *
 */
public interface INormalizer extends Serializable {
	
	/**
	 * Normalizes a rating.
	 * 
	 * @param rating The rating as it appeared in the data set.
	 * @return the normalized rating: [0.0, 10.0].
	 */
	public double normalizeRating(double rating);
}
