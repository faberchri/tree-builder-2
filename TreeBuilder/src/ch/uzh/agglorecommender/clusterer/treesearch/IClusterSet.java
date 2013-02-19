package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.Collection;
import java.util.Set;

public interface IClusterSet<E> {
	
	/**
	 * Checks if the ClusterSet contains the passed object.
	 * @param o the object to look up
	 * @return true if object is contained, else false.
	 */
	public abstract boolean contains(Object o);

	/**
	 * Removes an object from this ClusterSet and 
	 * updates the combinationsMap and combinationIndices.
	 * @param o the object to remove
	 * @return false if object was not contained in this ClusterSet, else true
	 */
	public abstract boolean remove(Object o);

	/**
	 * Adds a new element to the ClusterSet if the passed element
	 * is not already contained in the ClusterSet.
	 * Updates the combinationsMap and combinationIndices.
	 * @param e element to add.
	 * @return false if passed element already present, else true
	 */
	public abstract boolean add(E e);

	/**
	 * Gets the size of the set.
	 * @return the size
	 */
	public abstract int size();

	/**
	 * Checks if this cluster set is already fully clustered.
	 * @return true if set is clustered, else false.
	 */
	public abstract boolean clusteringDone();

	/**
	 * Gets a read only view of the collection to cluster.
	 * The collection reflects changes to the underlying collection but
	 * the underlying collection can not be changed
	 * through the returned collection.
	 * @return a read only view of the collection to cluster
	 */
	public abstract Collection<E> getUnmodifiableView();

	/**
	 * Gets the root of a cluster set if clustering is completed.
	 * @return the root cluster of the cluster set if or 
	 * null if clusteringDone() == false or set was initialized with 0 leaves.
	 */
	public abstract E getRoot();

	/**
	 * Gets any cluster.
	 * <br>
	 * <br>
	 * Used for testing.
	 * @return any cluster
	 */
	public abstract E getAnyElement();

	/**
	 * Gets the Set of all possible cluster combinations with a size <= {@code MAX_SUBSET_SIZE}.
	 * @return the possible combinations.
	 */
	public abstract Set<Collection<E>> getCombinations();

	/**
	 * Gets the set of all possible cluster combinations with a size <= {@code MAX_SUBSET_SIZE}
	 * and the passed element as cluster component.
	 * @param element the element for which cluster combinations are queried
	 * @return all possible combinations containing the passed element
	 */
	public abstract Set<Collection<E>> getCombinations(E element);

}