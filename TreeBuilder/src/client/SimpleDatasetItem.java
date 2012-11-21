package client;

public class SimpleDatasetItem<T> implements DatasetItem<T>{

	private final T value;
	private final int userId;
	private final int contentId;
	
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
