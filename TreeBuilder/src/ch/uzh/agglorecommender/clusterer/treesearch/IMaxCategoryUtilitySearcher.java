package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.List;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

/**
 * 
 * Searches in a given set of nodes for
 * a subset of nodes. A merge of all nodes in the subset
 * results in a new node with the greatest category utility
 * of all possible merges.
 *
 */
public interface IMaxCategoryUtilitySearcher {

	/**
	 * Searches in {@code combinationsToCheck} for the node combinations for
	 * which a merge produces a new node with a high category utility value.
	 * 
	 * @param combinationsToCheck the node combinations to test.
	 * @return Set of IMergeResult with a high category utility.
	 */
	public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<List<INode>> combinationsToCheck);

	
}
