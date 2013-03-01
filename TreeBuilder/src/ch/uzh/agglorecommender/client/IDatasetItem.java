package ch.uzh.agglorecommender.client;

import com.google.common.collect.Multimap;

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
	public T getRating();

	/**
	 * The background information of the user.
	 */
	public Multimap<Object, Object> getUserMetaMap();
	
	/**
	 * The background information of the content.
	 */
	public Multimap<Object, Object> getContentMetaMap();

	public void addContentMetaData(Object attribute, Object value);

	public void addUserMetaData(Object attribute, Object value);
	
}
