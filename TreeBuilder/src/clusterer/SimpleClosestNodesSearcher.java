package clusterer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleClosestNodesSearcher implements ClosestNodesSearcher {

	@Override
	public List<Node> getClosestNodes(Set<Node> openNodes) {
		long time = System.currentTimeMillis();
		double closestDistance = Double.MAX_VALUE;
		List<Node> closestNodes = new ArrayList<Node>();
		Set<Node> subSet = new HashSet<Node>(openNodes);
		for (Node node : openNodes) {
			subSet.remove(node);
			for (Node node2 : subSet) {
				double tmpDi = node.getDistance(node2);
				if (tmpDi < closestDistance){
					closestDistance = tmpDi;
					closestNodes.clear();
					closestNodes.add(node);
					closestNodes.add(node2);
				}
//				System.out.println("calculation: "+node+", "+node2+" ("+tmpDi+")");
			}
		}
		if (closestNodes.size() > 1) {
			System.out.println("Closest nodes: "+closestNodes.get(0)+", "+closestNodes.get(1)+" ("+closestDistance+")");
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Time in getClosestNode() single: " + (double)time / 1000.0);
		return closestNodes;
	}
	
	@Override
	public void setNodeOfLastMerge(Node newNode) {
		// not needed
		
	}

}
