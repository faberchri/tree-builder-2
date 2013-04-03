package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;



/**
 * Updates a set of nodes with a new node.
 */
public interface INodeUpdater extends Serializable {
	
	/**
	 * Updates a set of nodes with a new node.
	 * 
	 * @param newNode with this node the nodesToUpdate is complemented.
	 * @param nodesToUpdate the set of nodes to update.
	 */
	public void updateNodes(INode newNode, Collection<INode> nodesToUpdate); // newNode is the node which resulted from last merge
}
