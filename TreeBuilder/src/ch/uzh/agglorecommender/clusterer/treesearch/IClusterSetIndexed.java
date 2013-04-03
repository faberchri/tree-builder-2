package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.set.TIntSet;

import java.util.Collection;

/**
 * A cluster set that manages stored elements by indices.
 *
 * @param <E> The type of object to store in the cluster set.
 */
public interface IClusterSetIndexed<E> extends IClusterSet<E> {

	/**
	 * Resolves the passed combination id to the corresponding
	 * collection of elements contained in the cluster set.
	 * 
	 * @param combinationId the combination id to resolve
	 * @return a collection of elements corresponding to the combination id.
	 * The collection contains null values if the combination id is unknown. 
	 */
	public Collection<E> getCombination(int combinationId);
	
	/**
	 * Gets a copy of all currently valid combination
	 * id's of elements contained in the set.
	 * 
	 * @return set of combination id's
	 */
	public TIntSet getCombinationsIds();
	
	/**
	 * Gets all valid combination id's for a particular
	 * element contained in the set.
	 * @param e the element to fetch the combination id's
	 * @return set of all valid combination id's for the element
	 * or null if the passed element is not known.
	 */
	public TIntSet getCombinationsIds(E e);
}
