package clusterer;

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
}
