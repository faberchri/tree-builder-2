package modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import clusterer.IClosestNodesSearcher;
import clusterer.INode;

public class MultipleClosestNodesSearcher implements IClosestNodesSearcher {

	@Override
	public List<INode> getClosestNodes(Set<INode> openNodes) {
		
		long time = System.currentTimeMillis();
		double closestDistance = Double.MAX_VALUE;
		List<INode> closestNodes = new ArrayList<INode>();
		Set<INode> subSet = new HashSet<INode>(openNodes);
		
		// Add all Nodes with closest Distance to closestNodes List
		for (INode openNode : openNodes) {
			subSet.remove(openNode);
			for (INode subSetNode : subSet) {
				double tmpDistance = openNode.getDistance(subSetNode);
				
				// Found closer distance -> all previous found nodes don't matter anymore
				if (tmpDistance < closestDistance){
					closestDistance = tmpDistance;
					closestNodes.clear();
					closestNodes.add(openNode);
					closestNodes.add(subSetNode); // is this good?
					System.out.println("Found new closest Distance beetween "+openNode+", "+subSetNode+" ("+tmpDistance+")");
				}
				
				// Found similar distance -> add node to collection
				if(tmpDistance == closestDistance){
					
					// Ignore already found pairs
					if(!(closestNodes.contains(openNode) && closestNodes.contains(subSetNode))){
						if(!closestNodes.contains(openNode)) {
							closestNodes.add(openNode);
						}
						if(!closestNodes.contains(subSetNode)){
							closestNodes.add(subSetNode);
						}
						System.out.println("Found similar distance for "+openNode+", "+subSetNode+" ("+tmpDistance+")");
						System.out.println("Current Closest Nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
					}
				}
			}
		}
		
		if (closestNodes.size() > 1) {
			System.out.println("Closest nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Time in getClosestNode() single: " + (double)time / 1000.0);
		return closestNodes;
	}
	

}
