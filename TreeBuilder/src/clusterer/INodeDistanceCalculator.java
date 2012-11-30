package clusterer;

import java.util.Set;

/**
 * Calculates the distance between two nodes. 
 *
 */
public interface INodeDistanceCalculator {
	
	/**
	 * Calculates the distance between two nodes.
	 * 
	 * @param n1 start node
	 * @param n2 end node
	 * @return the distance between the two passed nodes.
	 */
	public double calculateDistance(INode n1, INode n2);
	
	/**
	 * Calculates the distance between two nodes.
	 * 
	 * @param n1 start node
	 * @param n2 end node
	 * @param counter Counter class
	 * @param openNodes Set of open Nodes
	 * @return the distance between the two passed nodes.
	 */
	public double calculateDistance(INode n1, INode n2, Counter counter, Set<INode> openNodes); // Utility Distance Calculation
}
