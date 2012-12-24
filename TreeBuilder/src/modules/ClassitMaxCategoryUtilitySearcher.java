package modules;

import clusterer.INode;

public class ClassitMaxCategoryUtilitySearcher extends MaxCategoryUtilitySearcher {


	@Override
	protected double calculateCategoryUtility(INode[] possibleMerge) {
		// TODO
		
//		System.out.println("multiple closest Nodes searcher");
//		
//		long time = System.currentTimeMillis();
//		double closestDistance = Double.MAX_VALUE;
//		List<INode> closestNodes = new ArrayList<INode>();
//		Set<INode> subSet = new HashSet<INode>(openNodes);
//		
//		// Add all Nodes with closest Distance to closestNodes List
//		for (INode openNode : openNodes) {
//			subSet.remove(openNode); // Should prevent duplicate comparisons
//			
//			// Compare with all other nodes
//			for (INode subSetNode : subSet) {
//				double tmpDistance = openNode.getDistance(subSetNode, Counter.getInstance(), openNodes);
//				
//				// Found closer distance -> all previous found nodes don't matter anymore
//				if (tmpDistance < closestDistance){
//					closestDistance = tmpDistance;
//					closestNodes.clear();
//					closestNodes.add(openNode);
//					closestNodes.add(subSetNode); // is this good?
//					System.out.println("Found new closest Distance beetween "+openNode+", "+subSetNode+" ("+tmpDistance+")");
//				}
//				
//				// Found similar distance -> add node to collection
//				if(tmpDistance == closestDistance){
//					
//					// Ignore already found pairs
//					if(!(closestNodes.contains(openNode) && closestNodes.contains(subSetNode))){
//						if(!closestNodes.contains(openNode)) {
//							closestNodes.add(openNode);
//						}
//						if(!closestNodes.contains(subSetNode)){
//							closestNodes.add(subSetNode);
//						}
//						System.out.println("Found similar distance for "+openNode+", "+subSetNode+" ("+tmpDistance+")");
//						System.out.println("Current Closest Nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
//					}
//				}
//				
//				// Update comparison counter
//				Counter.getInstance().addComparison();
//			}
//		}
//		
//		if (closestNodes.size() > 1) {
//			System.out.println("Closest nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
//		}
//		time = System.currentTimeMillis() - time;
//		System.out.println("Time in getClosestNode() single: " + (double)time / 1000.0);
//		return closestNodes;
		
		return 0;
	}


	

}
