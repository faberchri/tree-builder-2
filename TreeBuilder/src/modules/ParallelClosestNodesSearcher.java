package modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INode;
import clusterer.IMergeResult;

public class ParallelClosestNodesSearcher implements IMaxCategoryUtilitySearcher {
	
	public List<INode> getClosestNodes(Set<INode> openNodes) {
//		long time = System.currentTimeMillis();
		
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		
		List<INode> nLi = new ArrayList<INode>(openNodes);
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
		IMergeResult bestND = null;
		for (ClosestNodeCalculator closestNodeCalculator : taskList) {
			if (bestND == null || bestND.getCategoryUtility() > closestNodeCalculator.getBestNodeDistance().getCategoryUtility()) {
				bestND = closestNodeCalculator.getBestNodeDistance();
			}
		}

		List<INode> res = bestND.getBothNode();

		System.out.println("Closest nodes: "+res.get(0)+", "+res.get(1)+" ("+bestND.getCategoryUtility()+")");

//		time = System.currentTimeMillis() - time;
//		System.out.println("Time in getClosestNode() parallel: " + (double)time / 1000.0);
		return res;
	}
	
	private class ClosestNodeCalculator extends Thread{
		
		private final List<Integer> jLi;
		private final List<INode> nLi;
		
		IMergeResult bestNodeDistance = null;
				
		public ClosestNodeCalculator(List<Integer> jLi, List<INode> nLi) {
			this.jLi = jLi;
			this.nLi = nLi;
		}

		@Override
		public void run() {
			for (Integer i : jLi) {
				IMergeResult tmpDi = nLi.get(i).getDistanceToClosestNode(nLi.subList(i + 1, nLi.size()));
				if (bestNodeDistance == null || bestNodeDistance.getCategoryUtility() > tmpDi.getCategoryUtility()) {
					bestNodeDistance = tmpDi;
				}
			}			
		}
		
		public IMergeResult getBestNodeDistance() {
			return bestNodeDistance;
		}
		
	}

	@Override
	public IMergeResult getMaxCategoryUtilityMerge(Set<INode> openNodes) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
