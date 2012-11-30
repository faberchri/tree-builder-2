package modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import clusterer.Counter;
import clusterer.IClosestNodesSearcher;
import clusterer.INode;

public class SimpleClosestNodesSearcher implements IClosestNodesSearcher {

	@Override
	public List<INode> getClosestNodes(Set<INode> openNodes) {
		long time = System.currentTimeMillis();
		double closestDistance = Double.MAX_VALUE;
		List<INode> closestNodes = new ArrayList<INode>();
		Set<INode> subSet = new HashSet<INode>(openNodes);
		for (INode node : openNodes) {
			subSet.remove(node);
			for (INode node2 : subSet) {
				double tmpDi = node.getDistance(node2,null,null);
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
	public List<INode> getClosestNodes(Set<INode> openNodes, Counter counter) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
