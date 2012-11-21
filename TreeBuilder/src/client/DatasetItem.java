package client;

public interface DatasetItem<T> {
	public int getUserId();
	public int getContentId();
	public T getValue(); // i.e. rating
}
