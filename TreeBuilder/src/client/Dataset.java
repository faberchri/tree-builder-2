package client;

import java.util.Iterator;


public interface Dataset <T extends Number> {
	public Iterator<DatasetItem<T>> iterateOverDatasetItems();
	public Normalizer<T> getNormalizer();
}
