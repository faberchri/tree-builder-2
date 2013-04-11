package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.Collection;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

/**
 * 
 * Stores the category utility of a potential merge of nodes.
 *
 */
public interface IMergeResult extends Comparable<IMergeResult>{
	
	/**
	 * Gets the category utility resulting from a merge of the
	 * referenced nodes.
	 * 
	 * @return the category utility
	 */
	public double getCategoryUtility();
		
	/**
	 * Gets the set of all nodes considered in this merge.
	 * 
	 * @return array with all nodes of the merge.
	 */
	public Collection<INode> getNodes();
}
