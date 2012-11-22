package client;

/**
 * Basic implementation of the {@code IDatasetItem} interface.
 * Stores user id - content id - rating combinations
 *
 * @param <T> the data type of a raw rating.
 */
public class SimpleDatasetItem<T> implements IDatasetItem<T>{

	/**
	 * The rating.
	 */
	private final T value;
	
	/**
	 * The user id.
	 */
	private final int userId;
	
	/**
	 * The content id.
	 */
	private final int contentId;
	
	/**
	 * Instantiates a new {@code SimpleDataSetItem} which
	 * represents a user-content-rating combination.
	 * 
	 * @param value the raw rating.
	 * @param userId the id of the user of the rating.
	 * @param contentId the id of the rated content.
	 */
	public SimpleDatasetItem(T value, int userId, int contentId) {
		this.value = value;
		this.userId = userId;
		this.contentId = contentId;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public int getContentId() {
		return contentId;
	}

}
