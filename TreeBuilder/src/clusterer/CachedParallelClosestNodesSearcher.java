package clusterer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CachedParallelClosestNodesSearcher implements ClosestNodesSearcher {
	
	private Set<Node> openUserNodes = null;
	private Set<Node> openContentNodes = null;
	private boolean initialized = false;
	
	// Allows concurrent modification of the Map from several threads without the need to block them. 
	private ConcurrentHashMap<Node, NodeDistance> userDistanceCache = new ConcurrentHashMap<Node, NodeDistance>();
	private ConcurrentHashMap<Node, NodeDistance> contentDistanceCache = new ConcurrentHashMap<Node, NodeDistance>(); 

	private Node nodeCreatedInLastMerge = null;
	
	private ConcurrentLinkedQueue<PrintableNode> test = new ConcurrentLinkedQueue<PrintableNode>();

	private void init(Set<Node> openNodes, Node n) {
//		Iterator<Node> i = openNodes.iterator();
//		if (! i.hasNext()) return;
//		Node n = i.next();
		if (n.getClass().equals(UserNode.class)) {
			this.openUserNodes = openNodes;
		}
		if (n.getClass().equals(MovieNode.class)) {
			this.openContentNodes = openNodes;
		}
		if (openUserNodes != null && openContentNodes != null) {
			this.initialized = true;
		}
	}
	
	public List<Node> getClosestNodes(Set<Node> openNodes) {
		Iterator<Node> i = openNodes.iterator();
		if (! i.hasNext()) return null;
		Node n = i.next();
		NodeDistance result = null;
		if(! initialized) {
			init(openNodes, n);
			result = getClosestNodesByTotalRecalculation(openNodes);
		} else {
			updateSameHiearchyCache();
			updateOppositeHiearchyCache();
			if (openContentNodes.contains(n)) {
				contentDistanceCache.keySet().retainAll(openContentNodes);
				result = Collections.min(contentDistanceCache.values());

			} else {
				userDistanceCache.keySet().retainAll(openUserNodes);
				result = Collections.min(userDistanceCache.values());
			}
//			System.err.println("Err: We found a node, which does not belong to any set in :" + getClass().getSimpleName());
//			System.exit(-1);
//			return null;
		}
		List<Node> resLi = result.getBothNode();
//		removeResultFromCache(resLi);
//		PrintableNode[] arr = test.toArray(new PrintableNode[test.size()]);
//		Arrays.sort(arr, new NodeIdComparator());
//		for (PrintableNode printableNode : arr) {
//			System.out.println(printableNode);
//		}
		return resLi;
	}
		
//	private NodeDistance getClosestNodesFromCachedDistances(Set<Node> openNodes, ConcurrentHashMap<Node, NodeDistance> cache){
//		
////		// update cached distances of nodes of same type as previous merge result
////		Set<Node> previousSet = null;
////		ConcurrentHashMap<Node, NodeDistance> previousCache = null;
////		if (nodeCreatedInLastMerge instanceof UserNode) {
////			previousCache = userDistanceCache;
////			previousSet = openUserNodes;
////		} else {
////			previousCache = contentDistanceCache;
////			previousSet = openContentNodes;			
////		}
////		for (Node node : previousSet) {
////			if (node.equals(nodeCreatedInLastMerge)) continue;
////			double tmp = node.getDistance(nodeCreatedInLastMerge);
////			NodeDistance cachedDist = previousCache.get(node);
////			
//////			int cacheSize = cache.size();
//////			if ( cachedDist == null) continue; 
////			
////			if (tmp < cachedDist.getDistance()) {
////				previousCache.put(node, new SimpleNodeDistance(tmp, node, nodeCreatedInLastMerge));
////			}
////		}
////		
////		updateOppositeHiearchyNodes();
////		
//////		Set<Node> intersect = new HashSet<Node>(openNodes);
//////		intersect.retainAll(affectedNodes);
//
//		return Collections.min(cache.values());
//
//	}
	
	private void updateSameHiearchyCache() {
		// update cached distances of nodes of same type as previous merge result
		Set<Node> previousSet = null;
		ConcurrentHashMap<Node, NodeDistance> previousCache = null;
		if (nodeCreatedInLastMerge instanceof UserNode) {
			previousCache = userDistanceCache;
			previousSet = openUserNodes;
		} else {
			previousCache = contentDistanceCache;
			previousSet = openContentNodes;			
		}
		for (Node node : previousSet) {
			if (node.equals(nodeCreatedInLastMerge)) continue;
			double tmp = node.getDistance(nodeCreatedInLastMerge);
			NodeDistance cachedDist = previousCache.get(node);
			
//			int cacheSize = cache.size();
//			if ( cachedDist == null) continue; 
			
			if (tmp < cachedDist.getDistance()) {
				previousCache.put(node, new SimpleNodeDistance(tmp, node, nodeCreatedInLastMerge));
			}
		}
	}
	
	private void updateOppositeHiearchyCache() {
		// update cached distances of opposite type
		Set<Node> previousSet = null;
		ConcurrentHashMap<Node, NodeDistance> previousCache = null;
		if (nodeCreatedInLastMerge instanceof MovieNode) {
			previousCache = userDistanceCache;
			previousSet = openUserNodes;
		} else {
			previousCache = contentDistanceCache;
			previousSet = openContentNodes;			
		}
		Set<Node> affectedNodes = nodeCreatedInLastMerge.getAttributeKeys();
		for (Node affectedNode : affectedNodes) {
			for (Node openNode : previousSet) {
				if (openNode.equals(affectedNode)) continue;
				double tmp = openNode.getDistance(affectedNode);
//				if (cache.get(openNode) == null) continue;
				
				if (tmp < previousCache.get(openNode).getDistance()) {
					previousCache.put(openNode, new SimpleNodeDistance(tmp, openNode, affectedNode));
				}
				if (tmp < previousCache.get(affectedNode).getDistance()) {
					previousCache.put(affectedNode, new SimpleNodeDistance(tmp, affectedNode, openNode));
				}
			}
		}
	}
	
	private void removeMergedNodesFromCache(Node newNode) {
//		if (userDistanceCache.contains(resLi.get(0))){
//			for (Node node : resLi) {
//				userDistanceCache.remove(node);
//			}	
//		} else {
//			for (Node node : resLi) {
//				contentDistanceCache.remove(node);
//			}	
//		}
		Iterator<Node> i = newNode.getChildren();
		while (i.hasNext()) {
			Node node = (Node) i.next();
			contentDistanceCache.remove(node);
			userDistanceCache.remove(node);
		}
	}
	
	@Override
	public void setNodeOfLastMerge(Node newNode) {
		this.nodeCreatedInLastMerge = newNode;
		removeMergedNodesFromCache(newNode);
		// and add to cache with closest node
		if (openUserNodes.contains(newNode)) {
			userDistanceCache.put(newNode, newNode.getDistanceToClosestNode(new ArrayList<Node>(openUserNodes)));
		} else {
			contentDistanceCache.put(newNode,newNode.getDistanceToClosestNode(new ArrayList<Node>(openContentNodes)));
		}

	}
		
	private NodeDistance getClosestNodesByTotalRecalculation(Set<Node> openNodes) {
//		long time = System.currentTimeMillis();
		
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		
		List<Node> nLi = new ArrayList<Node>(openNodes);
		int longIndex = 0;
		int shortIndex = nLi.size() -1 ;
		
		List<List<Integer>> jLi = new ArrayList<List<Integer>>(numOfThreads);
		for (int i = 0; i < numOfThreads; i++) {
			jLi.add(new ArrayList<Integer>());
		}

		int currentJLi = 0;
//		List<Integer> test2 = new ArrayList<Integer>();
		while (longIndex <= shortIndex) {
			if (currentJLi >= numOfThreads) {
				currentJLi = 0;
			}
			jLi.get(currentJLi).add(new Integer(longIndex));
//			test2.add(new Integer(longIndex));
			if (shortIndex != longIndex) {
				jLi.get(currentJLi).add(new Integer(shortIndex));
			}
//			test2.add(new Integer(shortIndex));
			currentJLi++;
			longIndex++;
			shortIndex--;
		}
//		Collections.sort(test2);
//		for (Integer integer : test2) {
//			System.out.println("run index: " + integer);
//		}
		
		ExecutorService taskExecutor = Executors.newFixedThreadPool(numOfThreads);
		List<ClosestNodeCalculator> taskList = new ArrayList<ClosestNodeCalculator>();
		for (List<Integer> list : jLi) {
			ClosestNodeCalculator task = new ClosestNodeCalculator(list, nLi);
			taskList.add(task);
			taskExecutor.execute(task);
		}
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeDistance bestND = null;
		for (ClosestNodeCalculator closestNodeCalculator : taskList) {
			if (bestND == null || bestND.getDistance() > closestNodeCalculator.getBestNodeDistance().getDistance()) {
				bestND = closestNodeCalculator.getBestNodeDistance();
			}
		}
		return bestND;
		
//		List<Node> res = bestND.getBothNode();
//
//		System.out.println("Closest nodes: "+res.get(0)+", "+res.get(1)+" ("+bestND.getDistance()+")");
//
////		time = System.currentTimeMillis() - time;
////		System.out.println("Time in getClosestNode() parallel: " + (double)time / 1000.0);
//		return res;

	}
	
	private class ClosestNodeCalculator extends Thread{
		
		private final List<Integer> jLi;
		private final List<Node> nLi;
		
		NodeDistance bestNodeDistance = null;
				
		public ClosestNodeCalculator(List<Integer> jLi, List<Node> nLi) {
			this.jLi = jLi;
			this.nLi = nLi;
		}

		@Override
		public void run() {
			for (Integer i : jLi) {
//				if (i == nLi.size() - 1) continue;
				Node currentNode = nLi.get(i);
				NodeDistance tmpDi = currentNode.getDistanceToClosestNode(nLi.subList(i + 1, nLi.size()));
				if (tmpDi == null) System.out.println(currentNode);
//				if (openContentNodes.contains(currentNode)) {
				if (currentNode instanceof MovieNode) {
					contentDistanceCache.put(currentNode, tmpDi);
					
				} else {
					userDistanceCache.put(currentNode, tmpDi);
				}
				test.add((PrintableNode)currentNode);
				if (bestNodeDistance == null || bestNodeDistance.getDistance() > tmpDi.getDistance()) {
					bestNodeDistance = tmpDi;
				}
			}			
		}
		
		public NodeDistance getBestNodeDistance() {
			return bestNodeDistance;
		}
		
	}
	
}
