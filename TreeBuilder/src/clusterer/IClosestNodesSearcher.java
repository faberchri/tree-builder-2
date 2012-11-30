package clusterer;

import java.util.List;
import java.util.Set;

/**
 * 
 * Searches in a passed set of nodes
 * for the subset of nodes with the
 * shortest distance among each other.
 *
 */
public interface IClosestNodesSearcher {
	
	/**
	 * Searches in {@code openNodes} for closest nodes.
	 * 
	 * @param openNodes Set of nodes to search in.
	 * @return List of closest nodes.
	 */
	public List<INode> getClosestNodes(Set<INode> openNodes);
	
	/**
	 * Searches in {@code openNodes} for closest nodes.
	 * 
	 * @param openNodes Set of nodes to search in.
	 * @param counter Counter Class
	 * @return List of closest nodes.
	 */
	public List<INode> getClosestNodes(Set<INode> openNodes, Counter counter);

	
}
