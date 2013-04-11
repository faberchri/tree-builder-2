package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.set.TIntSet;

import java.io.Serializable;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

/**
 * 
 * Searches in a given set of nodes for
 * a subset of nodes. A merge of all nodes in the subset
 * results in a new node with the greatest category utility
 * of all possible merges.
 *
 */
public interface IMaxCategoryUtilitySearcher  extends Serializable {
	
	/**
	 * Searches in {@code combinationsIds} for node combinations with a high category utility value.
	 * 
	 * @param combinationIds the node combination id's to test.
	 * @param clusterSet the node set of the current clustering cycle
	 * @return A mapping of combination id's to the category utility of the merge.
	 */
	public TIntDoubleMap getMaxCategoryUtilityMerges(TIntSet combinationIds, IClusterSetIndexed<INode> clusterSet);
	
}
