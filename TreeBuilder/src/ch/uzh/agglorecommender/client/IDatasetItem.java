package ch.uzh.agglorecommender.client;

import java.util.Map;

/**
 * 
 * A data item is a single rating for one content item of one user.
 *
 * @param <T> the data type of the rating
 */
public interface IDatasetItem<T> {
	
	/**
	 * The id of the user, who submitted the rating.
	 * 
	 * @return the id of the user
	 */
	public int getUserId();
	
	/**
	 * The id of the content item of the rating.
	 * 
	 * @return the id of the content item.
	 */
	public int getContentId();
	
	/**
	 * The rating value.
	 * 
	 * @return the rating
	 */
	public T getValue(); // i.e. rating

	public Map<Object, Object> getUserMetaMap();
	
	public Map<Object, Object> getContentMetaMap();
	
}
