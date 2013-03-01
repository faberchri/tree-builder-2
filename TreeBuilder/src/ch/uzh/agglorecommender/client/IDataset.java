package ch.uzh.agglorecommender.client;

import java.util.Iterator;

/**
 * 
 * @author fabian
 * 
 * An abstraction of a data set.
 * Provides means to fetch the input data from a data set as data items.
 *
 * @param <T> the data type of the rating
 */
public interface IDataset <T extends Number> {
	
	/**
	 * Retrieve the data of the data set.
	 * 
	 * @return an iterator over all data set items in the data set.
	 */
	public Iterator<IDatasetItem<T>> iterateOverDatasetItems();
	
	/**
	 * Gets a normalizer for the data set.
	 * 
	 * @return the corresponding normalizer.
	 */
	public INormalizer<T> getNormalizer();
	
	public enum DataSetSplit{
		TRAINING, TEST
	}
}
