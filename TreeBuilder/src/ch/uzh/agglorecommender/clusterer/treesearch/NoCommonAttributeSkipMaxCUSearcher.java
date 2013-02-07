package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.Sets;

public class NoCommonAttributeSkipMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements
		IMaxCategoryUtilitySearcher, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	Set<List<INode>> combinationsWithSharedAttributes = new HashSet<List<INode>>();
	
	public NoCommonAttributeSkipMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(
			Set<List<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		int removedLists = 0;
		int initCombinationsSize = combinationsToCheck.size();
		Iterator<List<INode>> i = combinationsToCheck.iterator();
		while (i.hasNext()) {
			List<INode> l =  i.next();
			if (combinationsWithSharedAttributes.contains(l)) continue;
			if (l.size() != 2) continue;
			Set<INode> intersection = Sets.intersection(l.get(0).getAttributeKeys(), l.get(1).getAttributeKeys());
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

}
