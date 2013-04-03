package ch.uzh.agglorecommender.client;

import java.io.Serializable;
import java.util.Iterator;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * An abstraction of a data set.
 * Provides means to fetch the input data from a data set as data items.
 * 
 */
public interface IDataset extends Serializable{
	
	/**
	 * Retrieve the data of the data set.
	 * 
	 * @return an iterator over all data set items in the data set.
	 */
	public Iterator<IDatasetItem> iterateOverDatasetItems();
	
	/**
	 * Gets a normalizer for the data set.
	 * 
	 * @return the corresponding normalizer.
	 */
	public INormalizer getNormalizer();
	
	/**
	 * Gets a immutable map that indicates for each attribute (-tag) if it should be used for clustering.
	 * @return map of attribute-tags to boolean values 
	 */
	public ImmutableMap<String, Boolean> getAttributeClusteringConfig();
	
	/**
	 * Scales the passed value to the range defined in the data set property files.
	 * @param value a normalized value
	 * @param attributeTag the identifier of the attribute
	 * @return the attribute value in the original scale
	 */
	public double denormalize(double value, String attributeTag);
	
}
