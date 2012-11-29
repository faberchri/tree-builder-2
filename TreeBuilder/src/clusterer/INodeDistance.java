package clusterer;

import java.util.List;

/**
 * 
 * Stores the distance between two nodes.
 *
 */
public interface INodeDistance extends Comparable<INodeDistance>{
	
	/**
	 * Gets the distance between the two
	 * nodes of this {@code INodeDistance} object.
	 * 
	 * @return the distance
	 */
	public double getDistance();
	
	/**
	 * Gets the node of this {@code INodeDistance} object,
	 * which was not passed as argument.
	 * 
	 * @param knownNode a node which is part of this distance.
	 * @return the other node of this distance
	 * or null if the passed node is not part of this distance.
	 */
	public INode getOtherNode(INode knownNode);
	
	/**
	 * Gets the list of all nodes of this distance.
	 * 
	 * @return list with all nodes of this distance.
	 */
	public List<INode> getBothNode();
}
