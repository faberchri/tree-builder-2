package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.math3.util.ArithmeticUtils;

import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.List$;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * NOT USED.
 * 
 * Stores the objects to cluster. With getCombinationsList()
 * a set of all possible combinations (merges) up to the specified MAX_SUBSET_SIZE
 * can be retrieved.
 *
 * @param <E> the type of objects to cluster
 */
public class ClusterSet<E> implements Serializable, IClusterSet<E> {
	
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
	private Set<scala.collection.immutable.List<Short>> combinationIndices;
	
	/**
	 * Maps to each currently valid comparison index all possible comparisons (multimap).
	 * Used for keeping the combinationIndices set in sync with the cluster set.
	 */
	private HashMultimap<Short, scala.collection.immutable.List<Short>> combinationsMap;
	
	private Map<scala.collection.immutable.List<Short>, List<E>> cachedNodeLists
			= new HashMap<scala.collection.immutable.List<Short>, List<E>>();
	
	/**
	 * Instantiates a new cluster set.
	 * @param leafNodes the leaves of the cluster tree to create.
	 */
	public ClusterSet(ImmutableCollection<E> leafNodes) {
		
		this.openNodes.addAll(leafNodes);
		int numOfInitCombs = calcNumberOfInitialCombinations();
		this.combinationIndices = new HashSet<scala.collection.immutable.List<Short>>(numOfInitCombs);
		initializeCombinationIndices(numOfInitCombs);
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
		return res;
	}
	
	/**
	 * Adds a mapping of each index to all the combinations that involves this index. 
	 */
	private void initializeCombinationsMap() {
		for (scala.collection.immutable.List<Short> l : combinationIndices) {
			scala.collection.Iterator<Short> i = l.iterator();
			while(i.hasNext()) {
				Short n = i.next();
				combinationsMap.put(n, l);
			}			
		}
	}
	

	/**
	 * Calculates all possible combinations,limited to subset size <= MAX_SUBSET_SIZE,
	 * of the indices of the openNodes set (IndexAwareSet).
	 * The resulting index lists are stored in combinationIndices.
	 */
	private void initializeCombinationIndices(int initialNumberOfCombinations) {
		Logger log = TBLogger.getLogger(this.getClass().getName());
		if (MAX_SUBSET_SIZE == 2) {
			initializeDualCombinationIndices(initialNumberOfCombinations);
		} else {
			for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
				if (openNodes.size() < sublistLength) break;
				scala.collection.Iterator<scala.collection.immutable.List<Object>> it
							= SubsetsGenerator.subsets(openNodes.size(), sublistLength);
				int c = 0;
				while (it.hasNext()) {
					scala.collection.immutable.List<Short> sublist =
							(scala.collection.immutable.List<Short>) (scala.collection.immutable.List<?>) it.next();
					combinationIndices.add(sublist);
					if (c % 100000 == 0){
						log.info(c + " of " + initialNumberOfCombinations + " combination indice lists fetched. most recent list: " + sublist);
					}
					c++;
				}
			}			
		}
	}
	
	private void initializeDualCombinationIndices(int initialNumberOfCombinations) {
		Logger log = TBLogger.getLogger(this.getClass().getName());
		int c = 0;
		scala.collection.immutable.List sublist = null;
		for (short i = 0; i < openNodes.size(); i++) {
			for (short j = (short) (i + 1); j < openNodes.size(); j++) {
				sublist = newScalaList(i, j);
				combinationIndices.add(sublist);
				c++;
				if (c % 100000 == 0){
					log.info(c + " of " + initialNumberOfCombinations + " combination indice lists created. most recent list: " + sublist);
				}
			}
		}
		log.info(c + " initial combination indices list created. Expected: " + initialNumberOfCombinations + ", most recent list: " + sublist);
	}
	
    @SafeVarargs
	private static <T> scala.collection.immutable.List<T> newScalaList(T ... ts) {
    	scala.collection.immutable.List<T> result = List$.MODULE$.empty();
        for(int i = ts.length; i > 0; i--) {
            result = new $colon$colon(ts[i - 1], result);
        }
        return result;
    }
    
    private static scala.collection.immutable.List<Short> newScalaList(scala.collection.immutable.List<Integer> li) {
    	scala.collection.immutable.List<Short> result = List$.MODULE$.empty();
    	scala.collection.Iterator<Integer> it = li.reverseIterator();
    	while (it.hasNext()) {
			short s = it.next().shortValue();
			result = new $colon$colon(s, result);
		}
        return result;
    }
	
	/**
	 * Is called upon addition of a node to the openSet and updates 
	 * combinationsMap and combinationIndices with the new combination
	 * indices lists (limited to sublist length <= MAX_SUBSET_SIZE).
	 *
	 */
	private void updateCombinationIndices(short newIndex) {
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
					short n = i.next().shortValue();
					if (! combinationsMap.containsKey(n) && n != newIndex) {
						isValidList = false;
					}
				}
				if (! isValidList) continue;
				
				// update combinationIndicesMap if sublist is valid
				i = sublist.iterator();
				while(i.hasNext()) {
					short n = i.next().shortValue();
					if (combinationsMap.containsKey(n) || n == newIndex) {
						combinationsMap.put(n, newScalaList(sublist));	
					}
				}
				
			}
			combinationIndices.addAll(combinationsMap.get(newIndex));
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#remove(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return openNodes.contains(o);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {

		if (! openNodes.contains(o)) return false;

		short elementIndex = (short)openNodes.indexOf(o);
		Collection<scala.collection.immutable.List<Short>> c = combinationsMap.removeAll(elementIndex);
		combinationIndices.removeAll(c);
		return openNodes.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#add(E)
	 */
	@Override
	public boolean add(E e) {
		if (openNodes.contains(e)) return false;
		boolean b = openNodes.add(e);
		updateCombinationIndices((short)openNodes.getLastIndex());
		return b;

	}

	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#size()
	 */
	@Override
	public int size() {
		return openNodes.size();

	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#clusteringDone()
	 */
	@Override
	public boolean clusteringDone(){
		return size() < 2;
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#getUnmodifiableSetView()
	 */
	@Override
	public Collection<E> getUnmodifiableView() {
		return Collections.unmodifiableSet(openNodes);
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#getRoot()
	 */
	@Override
	public E getRoot() {
		if (! clusteringDone()) return null;
		if (size() == 0) return null;
		return openNodes.iterator().next();
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#getAnyElement()
	 */
	@Override
	public E getAnyElement() {
		E e = null;
		if (openNodes.iterator().hasNext()){
			e = openNodes.iterator().next();			
		}
		return e;
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#getCombinations()
	 */
	@Override
	public Set<Collection<E>> getCombinations() {
		Set<Collection<E>> combinations = new HashSet<Collection<E>>(combinationIndices.size());
		for (scala.collection.immutable.List<Short> l : combinationIndices) {
			combinations.add(convertIndexListToElementList(l));
		}
		return combinations;
	}
	
	private List<E> convertIndexListToElementList(scala.collection.immutable.List<Short> l) {
		if (cachedNodeLists.containsKey(l)) {
			return cachedNodeLists.get(l);
		} else {
			List<E> eL = new ArrayList<E>(l.size());
			scala.collection.Iterator<Short> i = l.iterator();
			while(i.hasNext()) {
				E e = openNodes.getByIndex(i.next());
				if (e == null) {
					Logger log = TBLogger.getLogger(getClass().getName());
					log.severe("Error in ClusterSet: Invalid index queried from IndexAwareSet!");
					System.exit(-1);
				}
				eL.add(e);			
			}
			cachedNodeLists.put(l, eL);
			return eL;
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet#getCombinations(E)
	 */
	@Override
	public Set<Collection<E>> getCombinations(E element) {
		short elIndex = (short)openNodes.indexOf(element);
		if (elIndex == -1) return null;
		Set<scala.collection.immutable.List<Short>> intersection = Sets.intersection(combinationsMap.get(elIndex), combinationIndices);
		
		Set<Collection<E>> combinations = new HashSet<Collection<E>>(intersection.size());
		for (scala.collection.immutable.List<Short> l : intersection) {
			combinations.add(convertIndexListToElementList(l));
		}
		return combinations;
	}
}
