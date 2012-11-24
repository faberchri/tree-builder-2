package client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import clusterer.INodeDistanceCalculator;
import clusterer.SimpleClosestNodesSearcher;
import clusterer.SimpleNodeUpdater;
import clusterer.TreeBuilder;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.plugin.Plugin;

public class TestDriver {

	private static final String rapidminerOperatorJarFileName = "treebuilder.jar";

	/**
	 * @param args args[0]: node distance calculator of user nodes. Fully qualified class name of desired implementation of INodeDistanceCalculator.
	 * <br>args[1]: node distance calculator of content nodes. Fully qualified class name of desired implementation of INodeDistanceCalculator.
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			printUsage();
		}

		INodeDistanceCalculator ndcContents = null;
		INodeDistanceCalculator  ndcUsers = null;

		try {
			ndcUsers = (INodeDistanceCalculator) Class.forName(args[0]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Err: Class " + args[0] + " was not found.");
			printUsage();
		} 

		try {
			ndcContents = (INodeDistanceCalculator) Class.forName(args[1]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			System.err.println("Err: Class " + args[1] + " was not found.");
			printUsage();
		}


		// get URL of treebuilder.jar from classpath -> needed for call to operator constructor of rapidminer
		URL[] urls = ((URLClassLoader) TestDriver.class.getClassLoader()).getURLs();
		URL myURL = null;
		for (URL url : urls) {
			if (url.getPath().endsWith(rapidminerOperatorJarFileName)) {
				myURL = url;
			}   
		}

		OperatorDescription rapidminerOperatorDescription = null;
		try {
			rapidminerOperatorDescription = new OperatorDescription(
					"groupKey",
					"key",
					TreeBuilder.class,
					TreeBuilder.class.getClassLoader(),
					"iconName",
					new Plugin(new File(myURL.getFile())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// create a data set
		RandomDataset ds = new RandomDataset(15, 10, 60);
		ds.printRandomMatrix();

		//		GrouplensDataset ds = new GrouplensDataset(null);
		//		TreeBuilder<Integer> treeBuilder = new TreeBuilder<Integer>(

		// create TreeBuilder
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
