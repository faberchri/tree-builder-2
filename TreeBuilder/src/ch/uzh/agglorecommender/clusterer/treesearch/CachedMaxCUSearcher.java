package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.impl.Constants;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.set.TIntSet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.util.TBLogger;


/**
 * Decorates a {@codeIMaxCategoryUtilitySearcher} by caching category
 * utility values calculated in a previous cycle.
 */
public class CachedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default value in cache map.
	 */
	private static final double NO_CACHE_ENTRY_VALUE = -1;
		
	/**
	 * Maps combination ids to its category utility calculated in a previous cycle.
	 */
	private TIntDoubleMap numberCache = new TIntDoubleHashMap(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, -1, NO_CACHE_ENTRY_VALUE);
		
	public CachedMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}
	
	/**
	 * Fetches category utilities from {@code numberCache} if cache entry
	 * is still valid and removes the corresponding combination id
	 * from {@code combinationIds} in order to reduce the number of
	 * calculations to perform.
	 */
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
			if (dirtyCombs == null) continue;
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
}
