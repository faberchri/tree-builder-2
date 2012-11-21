package client;

import clusterer.SimpleClosestNodesSearcher;
import clusterer.SimpleNodeDistanceCalculator;
import clusterer.SimpleNodeUpdater;
import clusterer.TreeBuilder;

public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RandomDataset ds = new RandomDataset();
		ds.printRandomMatrix();

//		GrouplensDataset ds = new GrouplensDataset(null);
//		TreeBuilder<Integer> treeBuilder = new TreeBuilder<Integer>(
		TreeBuilder<Double> treeBuilder = new TreeBuilder<Double>(
				ds,
				new SimpleNodeDistanceCalculator(),
				new SimpleNodeDistanceCalculator(),
				new SimpleNodeUpdater(),
				new SimpleClosestNodesSearcher());
		treeBuilder.cluster();
		
		treeBuilder.cluster();
		treeBuilder.visualize();

	}

}
