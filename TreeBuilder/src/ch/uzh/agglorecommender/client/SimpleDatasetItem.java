package ch.uzh.agglorecommender.client;

import com.google.common.collect.Multimap;

/**
 * Basic implementation of the {@code IDatasetItem} interface.
 * Stores user id - content id - rating combinations
 *
 */
class SimpleDatasetItem implements IDatasetItem{
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The rating.
	 */
	private final double value;
	
	/**
	 * The user id.
	 */
	private final String userId;
	
	/**
	 * The content id.
	 */
	private final String contentId;
		
	/**
	 * Instantiates a new {@code SimpleDataSetItem} which
	 * represents a user-content-rating combination.
	 * 
	 * @param value the raw rating.
	 * @param userId the id of the user of the rating.
	 * @param contentId the id of the rated content.
	 */
	SimpleDatasetItem(double value,
			String userId,
			String contentId)  {
		this.value = value;
		this.userId = userId;
		this.contentId = contentId;
	}

	@Override
	public double getRating() {
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
		return null;
	}
	
	@Override
	public Multimap<String, Object> getNominalContentMetaMap() {
		return null;
	}
	
	@Override
	public Multimap<String, Double> getNumericalUserMetaMap() {
		return null;
	}

	@Override
	public Multimap<String, Double> getNumericalContentMetaMap() {
		return null;
	}

}
