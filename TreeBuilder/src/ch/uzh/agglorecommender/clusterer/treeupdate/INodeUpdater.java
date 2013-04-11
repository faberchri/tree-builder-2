package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;



/**
 * Updates attributes of nodes with a new node as attribute.
 */
public interface INodeUpdater extends Serializable {
	
	/**
	 * Updates the attributes of a set of of nodes with a new node as a new attribute.
	 * The {@code newNode} is the node which resulted from the last merge.
	 * 
	 * @param newNode with this node the attributes of nodesToUpdate are complemented.
	 * @param nodesToUpdate the set of nodes to update attributes.
	 */
	public void updateNodes(INode newNode, Collection<INode> nodesToUpdate);
}
