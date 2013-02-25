package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.impl.Constants;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.set.TIntSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.util.TBLogger;

public class CachedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static final double NO_CACHE_ENTRY_VALUE = -1;
	
	private static Map<Collection<INode>, IMergeResult> cache = new HashMap<Collection<INode>,IMergeResult>();
	
	/**
	 * Maps combination ids to its category utility calculated in a previous cycle.
	 */
	private TIntDoubleMap numberCache = new TIntDoubleHashMap(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, -1, NO_CACHE_ENTRY_VALUE);
		
	public CachedMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}
	
	@Override
	public TIntDoubleMap getMaxCategoryUtilityMerges(
			TIntSet combinationIds, IClusterSetIndexed<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time1 = System.nanoTime();
			
		// iterate over all invalid node combinations
		Set<INode> dirtyNodes = Node.getAllDirtyNodes();
		log.info("Number of dirty nodes: " + dirtyNodes.size());
		Iterator<INode> dirtyNodesIterator = dirtyNodes.iterator();		
		for ( int i = dirtyNodes.size(); i-- > 0; ) { // faster iteration by avoiding hasNext()
			TIntSet dirtyCombs = clusterSet.getCombinationsIds(dirtyNodesIterator.next());
			TIntIterator dirtyCombsIterator = dirtyCombs.iterator();
			for ( int j = dirtyCombs.size(); j-- > 0; ) {
				// add invalid combinations to cache with default category utility value
				numberCache.put(dirtyCombsIterator.next(), NO_CACHE_ENTRY_VALUE);
			}
			// set the dirty node to clean (which means removing it from the dirty set)
			dirtyNodesIterator.remove();
		}
		
		// initialize some performance indicators
		int initialNumberOfComparisons = combinationIds.size();
		int numberOfInvalidCacheEntries = 0;
		int numberOfUsedCacheEntries = 0;
		
		// initialize variables to store best cache entry of the sought combinations
		double maxCachedCU = -1.0;
		int bestCombinationId = -1;
		
		// reduce the combinations to calculate by fetching (now valid) cache entries
		// at the same time we lookup the entry with the highest utility in the cache of the sought combinations.
		TIntIterator it = combinationIds.iterator();
		for ( int j = combinationIds.size(); j-- > 0; ) {
			int i = it.next();
			
			// no need to continue if combination id is unknown
			if (! numberCache.containsKey(i)) continue;
			
			double cacheValue = numberCache.get(i);
			if(cacheValue == NO_CACHE_ENTRY_VALUE) {
				numberOfInvalidCacheEntries++;
			} else {
				// cache entry is valid, 
				// thus we remove the combination from the set of combinations
				// for which category utility will be calculated
				it.remove(); 
				numberOfUsedCacheEntries++;
				
				// store the cached entry if it is a new best category utility
				if (cacheValue > maxCachedCU) {
					maxCachedCU = cacheValue;
					bestCombinationId = i;
				}
			}
		}
		
		log.info("Initial number of comparisons: "+ initialNumberOfComparisons + ". Number of invalidated cache entries: "
						+ numberOfInvalidCacheEntries + ". Number of used cache entries: "+ numberOfUsedCacheEntries
						+ ". Number of remaining category utility calculations: " + combinationIds.size());				
		time1 = System.nanoTime() - time1;
		
		TIntDoubleMap newMerges = decoratedSearcher.getMaxCategoryUtilityMerges(combinationIds, clusterSet);
		
		long time2 = System.nanoTime();
		log.info("Number of new merge results to add to the cache: " + newMerges.size());
		
		// add the calculated combinations to the cache
		numberCache.putAll(newMerges);
		
		// add the best cached category utility for the sought combinations to the result map if one was found.
		if (bestCombinationId > -1) {
			newMerges.put(bestCombinationId, maxCachedCU);
		}

		time2 = System.nanoTime() - time2;
		log.info("Time in cache decorator: " + (double)(time1 + time2) / 1000000000.0 + " s");
		return newMerges;
	}
	
	/**
	 * Removes combinations list that contain dirty nodes
	 * (nodes whit recently changed attributes) from the cache.
	 * The dirty flags of all nodes contained in the passed 
	 * cluster set are set to false and valid results are
	 * fetched from the cache.
	 * 
	 * @param combinationsToCheck the node combinations to check
	 * @param clusterSet the set of the nodes to cluster
	 * @return a set with IMergeResults obtained from the cache 
	 */
	private Set<IMergeResult> revalidateAndFetchCache(Set<Collection<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		long time = System.nanoTime();
		Logger log = TBLogger.getLogger(getClass().getName());
		int invalidLists = cache.keySet().size();
		Set<IMergeResult> result = new HashSet<IMergeResult>(combinationsToCheck.size());
		Set<INode> dirty = Node.getAllDirtyNodes();
		int dirtyNodes = dirty.size();
		Iterator<INode> i = dirty.iterator();
		long step = System.nanoTime();
		while (i.hasNext()) {
			INode n = i.next();
			if (clusterSet.contains(n)){
				cache.keySet().removeAll(clusterSet.getCombinations(n));
				i.remove();
			}
		}
//		System.err.println("remove from cahce and dirty set: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		for (Collection<INode> l : combinationsToCheck) {
			result.add(cache.get(l));
		}
		result.remove(null);
//		System.err.println("add to result set: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		
		time = System.nanoTime() - time;
		log.info("Number of invalidated lists: " + (invalidLists - cache.keySet().size())
				+ ", number of cleaned dirty nodes: " + (dirtyNodes - dirty.size())
				+", time for cache update: "+(double)time / 1000000000.0 + " s");
		return result;
	}
	

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<Collection<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time1 = System.nanoTime();
		int initialNumberOfCombinationsToCheck = combinationsToCheck.size();
		
		long step = System.nanoTime();
//		// (re)validate cache
//		revalidateCache();
//		System.err.println("reval: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
//		step = System.nanoTime();
//		
//		// new result set
//		Set<IMergeResult> result = new HashSet<IMergeResult>(combinationsToCheck);
//		
//		// fetch cached valid results and add to results
//		Set<List<INode>> kS = Sets.intersection(cache.keySet(), combinationsToCheck);
//		for (List<INode> l : kS) {
//			result.add(cache.get(l));
//		}
		
		// revalidate the cache and fetch valid cache values into results
		Set<IMergeResult> result = revalidateAndFetchCache(combinationsToCheck, clusterSet);
		
//		System.err.println("reval / fetch cache: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		
		// remove cached lists from combinationsToCheck
		combinationsToCheck.removeAll(cache.keySet());
//		System.err.println("rmv cached lists: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));

	
		log.info(result.size() + " of " + initialNumberOfCombinationsToCheck + " IMergeResults were found in cache");
		time1 = System.nanoTime() - time1;
		
		// fetch new merge results
		Set<IMergeResult> newMerges = decoratedSearcher.getMaxCategoryUtilityMerges(combinationsToCheck, clusterSet);
		
		step = System.nanoTime();
		
		long time2 = System.nanoTime();
		
		// add new merge results to cache
		for (IMergeResult nM : newMerges) {
			cache.put(nM.getNodes(), nM);
		}
		
//		System.err.println("increase cache: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		
		// combine cached results and new results
		result.addAll(newMerges);
		
//		System.err.println("combine results: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));

		
		time2 = System.nanoTime() - time2;
		log.info("Time in cache decorator: " + (double)(time1 + time2) / 1000000000.0 + " s");
		return result;
	}
	
	/**
	 * Removes combinations list that contain dirty nodes
	 * (nodes whit recently changed attributes) from the cache.
	 * The dirty flags of all nodes contained in the cache are 
	 * reset to false.
	 * <br>
	 * <br>
	 * NOT USED CURRENTLY DUE TO LACK OF PERFORMANCE
	 */
	private void revalidateCache() {
		long time = System.nanoTime();
		Logger log = TBLogger.getLogger(getClass().getName());

		final Set<INode> dirtyNodes = Collections.synchronizedSet(new HashSet<INode>()) ;
		final Set<Collection<INode>> invalidLists = Collections.synchronizedSet(new HashSet<Collection<INode>>());
		
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (final Collection<INode> l : cache.keySet()) {
			futures.add(es.submit(new Runnable() {
				
				@Override
				public void run() {
					for (INode n : l) {
						if(n.isDirty()) {				
							invalidLists.add(l);
							dirtyNodes.add(n);
							break;
						}
					}					
				}
			}));
		}
		es.shutdown();
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				log.severe("InterruptedException or ExecutionException while waiting for termination.");
				System.exit(-1);
			}
		}
				
		// remove list with dirty node(s) from cache
		cache.keySet().removeAll(invalidLists);
		
		// reset dirty flags of nodes
		for (INode n : dirtyNodes) {
			n.setClean();
		}
		
		time = System.nanoTime() - time;
		log.info("Number of invalidated lists: " + invalidLists.size()
				+ ", number of dirty nodes: " + dirtyNodes.size()
				+", time for cache update: "+(double)time / 1000000000.0 + " s");
	}

}
