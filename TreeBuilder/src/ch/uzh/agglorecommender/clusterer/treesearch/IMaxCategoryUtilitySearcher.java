package ch.uzh.agglorecommender.clusterer.treesearch;

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
	 * Searches in {@code openNodes} for the nodes for
	 * which a merge produces a new node with the
	 * greatest category utility of all possible merges.
	 * 
	 * @param openNodes Set of nodes to search in.
	 * @return List of closest nodes.
	 */
	public IMergeResult getMaxCategoryUtilityMerge(Set<INode> openNodes);

	
}
