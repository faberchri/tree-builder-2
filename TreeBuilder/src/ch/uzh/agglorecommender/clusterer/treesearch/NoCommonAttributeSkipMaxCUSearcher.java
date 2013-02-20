package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.set.TIntSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.Sets;

public class NoCommonAttributeSkipMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	Set<Collection<INode>> combinationsWithSharedAttributes = new HashSet<Collection<INode>>();
	
	public NoCommonAttributeSkipMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(
			Set<Collection<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		int removedLists = 0;
		int initCombinationsSize = combinationsToCheck.size();
		Iterator<Collection<INode>> i = combinationsToCheck.iterator();
		while (i.hasNext()) {
			Collection<INode> l =  i.next();
			if (combinationsWithSharedAttributes.contains(l)) continue;
			if (l.size() != 2) continue;
			
			Iterator<INode> it = l.iterator();
			INode first = it.next();
			INode second = it.next();
			Set<INode> intersection = Sets.intersection(first.getAttributeKeys(), second.getAttributeKeys());
			if (intersection.size() == 0) {
				removedLists++;
				i.remove();
			} else {
				combinationsWithSharedAttributes.add(l);
			}		
		}
		log.info("Time in NoCommonAttributeSkipDecorator: "
				+ (double)(System.nanoTime() - time) / 1000000000.0 + " seconds, "
				+ "Number of removed comparisons: " + removedLists + " of " + initCombinationsSize);

		return decoratedSearcher.getMaxCategoryUtilityMerges(combinationsToCheck, clusterSet);
	}

	@Override
	public TIntDoubleMap getMaxCategoryUtilityMerges(
			TIntSet combinationIds, IClusterSetIndexed<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		int removedLists = 0;
		int initCombinationsSize = combinationIds.size();
		TIntIterator i = combinationIds.iterator();
		while (i.hasNext()) {
			Collection<INode> l =  clusterSet.getCombination(i.next());
			if (combinationsWithSharedAttributes.contains(l)) continue;
			if (l.size() != 2) continue;
			
			Iterator<INode> it = l.iterator();
			INode first = it.next();
			INode second = it.next();
			Set<INode> intersection = Sets.intersection(first.getAttributeKeys(), second.getAttributeKeys());
			if (intersection.size() == 0) {
				removedLists++;
				i.remove();
			} else {
				combinationsWithSharedAttributes.add(l);
			}		
		}
		log.info("Time in NoCommonAttributeSkipDecorator: "
				+ (double)(System.nanoTime() - time) / 1000000000.0 + " seconds, "
				+ "Number of removed comparisons: " + removedLists + " of " + initCombinationsSize);
		return decoratedSearcher.getMaxCategoryUtilityMerges(combinationIds, clusterSet);
	}
	
}
