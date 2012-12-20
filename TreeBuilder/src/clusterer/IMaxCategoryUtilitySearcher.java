package clusterer;

import java.util.Set;

/**
 * 
 * Searches in a passed set of nodes
 * for the subset of nodes with the
 * shortest distance among each other.
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
