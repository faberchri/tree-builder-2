package client;

import java.io.IOException;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.plugin.Plugin;

import clusterer.NodeDistanceCalculator;
import clusterer.SimpleClosestNodesSearcher;
import clusterer.SimpleNodeDistanceCalculator;
import clusterer.SimpleNodeUpdater;
import clusterer.TreeBuilder;

public class TestDriver {

	/**
	 * @param args args[0]: node distance calculator of user nodes. Fully qualified class name of desired implementation of NodeDistanceCalculator.
	 * <br>args[1]: node distance calculator of content nodes. Fully qualified class name of desired implementation of NodeDistanceCalculator.
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			printUsage();
		}
	
		NodeDistanceCalculator ndcContents = null;
		NodeDistanceCalculator  ndcUsers = null;

		try {
			ndcUsers = (NodeDistanceCalculator) Class.forName(args[0]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Err: Class " + args[0] + " was not found.");
			printUsage();
		} 

		try {
			ndcContents = (NodeDistanceCalculator) Class.forName(args[1]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			System.err.println("Err: Class " + args[1] + " was not found.");
			printUsage();
		}
		
		RandomDataset ds = new RandomDataset(15, 10, 60);
		ds.printRandomMatrix();
		
//		GrouplensDataset ds = new GrouplensDataset(null);
//		TreeBuilder<Integer> treeBuilder = new TreeBuilder<Integer>(
		
		OperatorDescription rapidminerOperatorDescription = null;
		try {
			rapidminerOperatorDescription = new OperatorDescription("groupKey", "key", TreeBuilder.class, TreeBuilder.class.getClassLoader(), "iconName", new Plugin(null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TreeBuilder<Double> treeBuilder = new TreeBuilder<Double>(
				rapidminerOperatorDescription,
				ds,
				ndcUsers,
				ndcContents,
				new SimpleNodeUpdater(),
				new SimpleClosestNodesSearcher());
		treeBuilder.cluster();
		
		treeBuilder.cluster();
		treeBuilder.visualize();

	}
	
	private static void printUsage() {
		System.out.println("Usage: <fully-qualified-class-name-of-NodeDistanceCalcuator-for-users> " +
				"<fully-qualified-name-of-NodeDistanceCalcuator-for-contents>\n" +
				"Example: clusterer.SimpleNodeDistanceCalculator clusterer.SimpleNodeDistanceCalculator");
		System.exit(-1);

	}
	
	
	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
