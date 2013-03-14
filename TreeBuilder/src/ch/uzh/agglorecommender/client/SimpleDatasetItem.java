package ch.uzh.agglorecommender.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Basic implementation of the {@code IDatasetItem} interface.
 * Stores user id - content id - rating combinations
 *
 * @param <T> the data type of a raw rating.
 */
public class SimpleDatasetItem<T extends Number> implements IDatasetItem<T>{

	/**
	 * The rating.
	 */
	private final T value;
	
	/**
	 * The user id.
	 */
	private final String userId;
	
	/**
	 * The content id.
	 */
	private final String contentId;
	
	/**
	 * maps a meta attribute 
	 * (e.g. age) to the items
	 * corresponding value (e.g. 20). Since the attribute can have different values
	 * (e.g. occupation: student and programmer) we need a multimap.
	 */
	private Multimap<String, Object> userMetaMap = HashMultimap.create();
	
	/**
	 * maps a meta attribute 
	 * (genre) to the items
	 * corresponding value (thriller). Since the attribute can have different values
	 * (e.g. genre: thriller and action) we need a multimap.
	 */
	private Multimap<String, Object> contentMetaMap = HashMultimap.create();

	
	/**
	 * Instantiates a new {@code SimpleDataSetItem} which
	 * represents a user-content-rating combination.
	 * 
	 * @param value the raw rating.
	 * @param userId the id of the user of the rating.
	 * @param contentId the id of the rated content.
	 */
	public SimpleDatasetItem(T value,
			String userId,
			String contentId)  {
		this.value = value;
		this.userId = userId;
		this.contentId = contentId;
	}

	@Override
	public T getRating() {
		return value;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getContentId() {
		return contentId;
	}
	
	@Override
	public Multimap<String, Object> getNominalUserMetaMap() {
		if (userMetaMap.size() == 0) return null;
		return userMetaMap;
	}
	
	@Override
	public Multimap<String, Object> getNominalContentMetaMap() {
		if (contentMetaMap.size() == 0) return null;
		return contentMetaMap;
	}
	
	@Override
	public void addUserMetaData(String attribute, Object value) {
		userMetaMap.put(attribute, value);
	}
	
	@Override
	public void addContentMetaData(String attribute, Object value) {
		contentMetaMap.put(attribute, value);
	}

	@Override
	public Multimap<String, Double> getNumericalUserMetaMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Multimap<String, Double> getNumericalContentMetaMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
