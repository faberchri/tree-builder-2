package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;



/**
 * Updates a set of nodes with a new node.
 */
public interface INodeUpdater {
	
	/**
	 * Updates a set of nodes with a new node.
	 * 
	 * @param newNode with this node the nodesToUpdate is complemented.
	 * @param nodesToUpdate the set of nodes to update.
	 */
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate); // newNode is the node which resulted from last merge
}
