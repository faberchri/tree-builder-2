package ch.uzh.agglorecommender.client;

import java.io.Serializable;

import com.google.common.collect.Multimap;

/**
 * 
 * A data item is a single rating for one content item of one user.
 *
 */
public interface IDatasetItem extends Serializable {
	
	/**
	 * The id of the user, who submitted the rating.
	 * 
	 * @return the id of the user
	 */
	public String getUserId();
	
	/**
	 * The id of the content item of the rating.
	 * 
	 * @return the id of the content item.
	 */
	public String getContentId();
	
	/**
	 * The rating value.
	 * 
	 * @return the rating
	 */
	public double getRating();

	/**
	 * The nominal background information of the user.
	 */
	public Multimap<String, Object> getNominalUserMetaMap();
	
	/**
	 * The nominal background information of the content.
	 */
	public Multimap<String, Object> getNominalContentMetaMap();
	
	/**
	 * The numerical background information of the user.
	 */
	public Multimap<String, Double> getNumericalUserMetaMap();
	
	/**
	 * The numerical background information of the content.
	 */
	public Multimap<String, Double> getNumericalContentMetaMap();
	
}
