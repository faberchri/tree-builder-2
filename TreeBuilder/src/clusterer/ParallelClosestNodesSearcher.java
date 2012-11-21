package clusterer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelClosestNodesSearcher implements ClosestNodesSearcher {
	
	public List<Node> getClosestNodes(Set<Node> openNodes) {
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
		
		while (longIndex <= shortIndex) {
			if (currentJLi >= numOfThreads) {
				currentJLi = 0;
			}
			jLi.get(currentJLi).add(new Integer(longIndex));
			jLi.get(currentJLi).add(new Integer(shortIndex));
			currentJLi++;
			longIndex++;
			shortIndex--;
		}
		
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
			e.printStackTrace();
		}
		NodeDistance bestND = null;
		for (ClosestNodeCalculator closestNodeCalculator : taskList) {
			if (bestND == null || bestND.getDistance() > closestNodeCalculator.getBestNodeDistance().getDistance()) {
				bestND = closestNodeCalculator.getBestNodeDistance();
			}
		}

		List<Node> res = bestND.getBothNode();

		System.out.println("Closest nodes: "+res.get(0)+", "+res.get(1)+" ("+bestND.getDistance()+")");

//		time = System.currentTimeMillis() - time;
//		System.out.println("Time in getClosestNode() parallel: " + (double)time / 1000.0);
		return res;
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
				NodeDistance tmpDi = nLi.get(i).getDistanceToClosestNode(nLi.subList(i + 1, nLi.size()));
				if (bestNodeDistance == null || bestNodeDistance.getDistance() > tmpDi.getDistance()) {
					bestNodeDistance = tmpDi;
				}
			}			
		}
		
		public NodeDistance getBestNodeDistance() {
			return bestNodeDistance;
		}
		
	}
	
	@Override
	public void setNodeOfLastMerge(Node newNode) {
		// not needed
		
	}
}
