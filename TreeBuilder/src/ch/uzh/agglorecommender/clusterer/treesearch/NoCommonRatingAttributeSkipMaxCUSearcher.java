package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 * Decorates a {@code IMaxCategoryUtilitySearcher} by removing potential merges from
 * {@code combinationIds} that do not share a ratings attribute.
 */
public class NoCommonRatingAttributeSkipMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 *  Keeps track of combinations which have been identified as combinations with shared nodes.
	 *  These combinations won't be tested on shared attributes in a subsequent clustering cycle again.
	 *  This means that combination id's contained in this set will never be removed from the 
	 *  set of combinations passed to @code{getMaxCategoryUtilityMerges} to calculate its
	 *  category utility by this decorator. 
	 */
	private TIntSet combinationsIndicesWithSharedAttributes = new TIntHashSet();
	
	public NoCommonRatingAttributeSkipMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}

	/**
	 * Removes from {@code combinationIds} potential merges of nodes that do not share 
	 * a rating attribute in order to reduce the number of calculations to perform. 
	 * If {@code combinationIds.size() < 2} no more combination ids are removed.
	 */
	@Override
	public TIntDoubleMap getMaxCategoryUtilityMerges(
			TIntSet combinationIds, IClusterSetIndexed<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		
		// initialize performance indicators
		int removedLists = 0;
		int initCombinationsSize = combinationIds.size();
		
		// iterate over the collection of possible combinations
		TIntIterator iterator = combinationIds.iterator();
		for ( int i = combinationIds.size(); i-- > 0; ) {  // faster iteration by avoiding hasNext()
			// leave at least one combinationId in list to calculate, otherwise null pointer
			if (combinationIds.size() < 2) break;
			
			int combination = iterator.next();
			
			// skip this combination if nodes are known to have shared attributes
			if (combinationsIndicesWithSharedAttributes.contains(combination)) continue;
			
			// get the attributes of both nodes of the combination
			Iterator<INode> it = clusterSet.getCombination(combination).iterator();
			Set<INode> attFirst = it.next().getRatingAttributeKeys();
			Set<INode> attSecond = it.next().getRatingAttributeKeys();
			
			// check if nodes of the combinations share an attribute
			boolean remove = true;
			for (INode aF : attFirst) {
				if (attSecond.contains(aF)) {
					// don't remove combination if nodes share an attribute
					remove = false;
					break;
				}			
			}
			 
			if (remove) {
				// remove combinations without shared attributes from the collection of possible merges
				removedLists++;
				iterator.remove();
			} else {
				// store the combination with a shared attribute for future speed up of this filter
				combinationsIndicesWithSharedAttributes.add(combination);
			}		
		}
		log.info("Time in NoCommonAttributeSkipDecorator: "
				+ (double)(System.nanoTime() - time) / 1000000000.0 + " seconds, "
				+ "Number of removed comparisons: " + removedLists + " of " + initCombinationsSize);
		return decoratedSearcher.getMaxCategoryUtilityMerges(combinationIds, clusterSet);
	}
	
}
