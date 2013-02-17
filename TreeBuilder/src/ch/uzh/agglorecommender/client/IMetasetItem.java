package ch.uzh.agglorecommender.client;

import java.util.Map;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;

/**
 * 
 * A data item is a single rating for one content item of one user.
 *
 * @param <T> the data type of the rating
 */
public interface IMetasetItem<T extends Map<String,String>> {
	
	/**
	 * The id of the item to which the meta info belongs
	 * 
	 * @return the id of the related item
	 */
	public int getItemId();
	
	/**
	 * The type of the related item
	 * 
	 * @return the type of the related item
	 */
	public ENodeType getItemType();
	
	/**
	 * The metadata
	 * 
	 * @return the metadata
	 */
	public T getValue();
}
