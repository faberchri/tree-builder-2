package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.math3.util.ArithmeticUtils;

import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;


/**
 * 
 * Stores the objects to cluster. With getCombinationsList()
 * a set of all possible combinations (merges) up to the specified MAX_SUBSET_SIZE
 * can be retrieved.
 *
 * @param <E> the type of objects to cluster
 */
public class ClusterSet<E> implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * All generated subsets have size <= MAX_SUBSET_SIZE.
	 * This limit reduces the number of generated sets from
	 * <br>
	 * <pre>
	 * C(set_size, 2) +  C(set_size, 3) + ... + C(set_size,set_size)
	 * </pre>
	 * <br>
	 * to
	 * <br>
	 * <pre>
	 * C(set_size, 2) +  C(set_size, 3) + ... + C(set_size, MAX_SUBSET_SIZE)
	 * </pre>
	 * <br>
	 * with n = set_size, m = length_of_sublist, and
	 * <br>
	 * <pre>
	 *                  n!
	 *  C(n,m) = ----------------
	 *              m! (n - m)!
	 * </pre>
	 * <br>
	 * Which is the binomial coefficient for nonnegative n and m:
	 * the number of ways of picking unordered outcomes from  possibilities,
	 * also known as a combination or combinatorial number. 
	 * <br>
	 * (Calculating the complete power set would result in generating 2^n subsets.)
	 * 
	 */
	private final int MAX_SUBSET_SIZE = 2;
	
	/**
	 * Stores the instances to cluster.
	 */
	private IndexAwareSet<E> openNodes = new IndexAwareSet<E>();
	
	/**
	 * A list of all currently valid comparisons indices. Used to
	 * generate a the set of valid node comparisons.
	 */
	private Set<scala.collection.immutable.List<Integer>> combinationIndices;
	
	/**
	 * Maps to each currently valid comparison index all possible comparisons (multimap).
	 * Used for keeping the combinationIndices set in sync with the cluster set.
	 */
	private Multimap<Integer, scala.collection.immutable.List<Integer>> combinationsMap;
	
	/**
	 * Instantiates a new cluster set.
	 * @param leafNodes the leaves of the cluster tree to create.
	 */
	public ClusterSet(ImmutableCollection<E> leafNodes) {
		
		this.openNodes.addAll(leafNodes);		
		this.combinationIndices = new HashSet<scala.collection.immutable.List<Integer>>(calcNumberOfInitialCombinations());
		initializeCombinationIndices();
		this.combinationsMap = HashMultimap.create() ;
		initializeCombinationsMap();
	}
	
	/**
	 * Calculate the total number of possible node combinations.
	 * Depends on the defined MAX_SUBSET_SIZE
	 * @return number of possible combinations
	 */
	private int calcNumberOfInitialCombinations() {
		int minSubsetSize = 2;
		int res = 0;
		while (minSubsetSize <= MAX_SUBSET_SIZE) {
			res = ArithmeticUtils.addAndCheck(res, Ints.checkedCast(ArithmeticUtils.binomialCoefficient(openNodes.size(), minSubsetSize)));
			minSubsetSize++;
		}
		System.err.println("num of init combs: " + res);
		return res;
	}
	
	/**
	 * Adds a mapping of each index to all the combinations that involves this index. 
	 */
	private void initializeCombinationsMap() {
		for (scala.collection.immutable.List<Integer> l : combinationIndices) {
			scala.collection.Iterator<Integer> i = l.iterator();
			while(i.hasNext()) {
				Integer n = i.next();
				combinationsMap.put(n, l);
			}			
		}
	}
	

	/**
	 * Calculates all possible combinations,limited to subset size <= MAX_SUBSET_SIZE,
	 * of the indices of the openNodes set (IndexAwareSet).
	 * The resulting index lists are stored in combinationIndices.
	 */
	private void initializeCombinationIndices() {
		for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
			if (openNodes.size() < sublistLength) break;
			scala.collection.Iterator<scala.collection.immutable.List<Object>> it
						= SubsetsGenerator.subsets(openNodes.size(), sublistLength);
			while (it.hasNext()) {
				scala.collection.immutable.List<Integer> sublist =
						(scala.collection.immutable.List<Integer>) (scala.collection.immutable.List<?>) it.next();
				combinationIndices.add(sublist);
			}
		}
	}
	
	/**
	 * Is called upon addition of a node to the openSet and updates 
	 * combinationsMap and combinationIndices with the new combination
	 * indices lists (limited to sublist length <= MAX_SUBSET_SIZE).
	 *
	 */
	private void updateCombinationIndices(int newIndex) {
		Logger log = TBLogger.getLogger(getClass().getName());

		for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
			scala.collection.Iterator<scala.collection.immutable.List<Object>> it 
					= SubsetsGenerator.remainingSubsets(newIndex, sublistLength);
			while (it.hasNext()) {
				scala.collection.immutable.List<Integer> sublist =
						(scala.collection.immutable.List<Integer>) (scala.collection.immutable.List<?>) it.next();
				
				// check if new sublist is valid
				boolean isValidList = true;
				scala.collection.Iterator<Integer> i = sublist.iterator();
				while(i.hasNext()) {
					Integer n = i.next();
					if (! combinationsMap.containsKey(n) && n != newIndex) {
						isValidList = false;
					}
				}
				if (! isValidList) continue;
				
				// update combinationIndicesMap if sublist is valid
				i = sublist.iterator();
				while(i.hasNext()) {
					int n = i.next();
					if (combinationsMap.containsKey(n) || n == newIndex) {
						combinationsMap.put(n, sublist);	
					}
				}
				
			}
			combinationIndices.addAll(combinationsMap.get(newIndex));
		}
	}

	/**
	 * Removes an object from this ClusterSet and 
	 * updates the combinationsMap and combinationIndices.
	 * @param o the object to remove
	 * @return false if object was not contained in this ClusterSet, else true
	 */
	public boolean remove(Object o) {

		if (! openNodes.contains(o)) return false;

		int elementIndex = openNodes.indexOf(o);
		Collection<scala.collection.immutable.List<Integer>> c = combinationsMap.removeAll(elementIndex);
		combinationIndices.removeAll(c);
		return openNodes.remove(o);
	}
	
	/**
	 * Adds a new element to the ClusterSet if the passed element
	 * is not already contained in the ClusterSet.
	 * Updates the combinationsMap and combinationIndices.
	 * @param e element to add.
	 * @return false if passed element already present, else true
	 */
	public boolean add(E e) {
		if (openNodes.contains(e)) return false;
		boolean b = openNodes.add(e);
		updateCombinationIndices(openNodes.getLastIndex());
		return b;

	}

	/**
	 * Gets the size of the set.
	 * @return the size
	 */
	public int size() {
		return openNodes.size();

	}
	
	/**
	 * Checks if this cluster set is already fully clustered.
	 * @return true if set is clustered, else false.
	 */
	public boolean clusteringDone(){
		return size() < 2;
	}
	
	/**
	 * Gets a read only view of the set to cluster.
	 * The set reflects changes to the underlying set but
	 * the underlying set can not be changed
	 * through the returned set.
	 * @return a read only view of the set to cluster
	 */
	public Set<E> getUnmodifiableSetView() {
		return Collections.unmodifiableSet(openNodes);
	}
	
	/**
	 * Gets the root of a cluster set if clustering is completed.
	 * @return the root cluster of the cluster set if or 
	 * null if clusteringDone() == false or set was initialized with 0 leaves.
	 */
	public E getRoot() {
		if (! clusteringDone()) return null;
		if (size() == 0) return null;
		return openNodes.iterator().next();
	}
	
	/**
	 * Gets any cluster.
	 * <br>
	 * <br>
	 * Used for testing.
	 * @return any cluster
	 */
	public E getAnyElement() {
		E e = null;
		if (openNodes.iterator().hasNext()){
			e = openNodes.iterator().next();			
		}
		return e;
	}
	
	/**
	 * Gets the Set of all possible cluster combinations with a size <= {@code MAX_SUBSET_SIZE}.
	 * @return the possible combinations.
	 */
	public Set<List<E>> getCombinations() {
		Set<List<E>> combinations = new HashSet<List<E>>(combinationIndices.size());
		for (scala.collection.immutable.List<Integer> l : combinationIndices) {
			List<E> eL = new ArrayList<E>(l.size());
			scala.collection.Iterator<Integer> i = l.iterator();
			while(i.hasNext()) {
				E e = openNodes.getByIndex(i.next());
				if (e == null) {
					System.err.println("null");
				}
				eL.add(e);
			}
			combinations.add(eL);
		}
		return combinations;
	}
}
