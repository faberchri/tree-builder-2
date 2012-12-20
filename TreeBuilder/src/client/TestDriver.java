package client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INodeDistanceCalculator;
import clusterer.INodeUpdater;
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

		if (args.length != 4) {
			printUsage();
		}

		INodeDistanceCalculator ndcContents = null;
		INodeDistanceCalculator  ndcUsers = null;
		IMaxCategoryUtilitySearcher maxCategoryUtilitySearcher = null;
		INodeUpdater nodeUpdater = null;
		
		// Content Nodes Distance Calculator Definition
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
		
		// Content Nodes Distance Calculator Definition// Content Nodes Distance Calculator Definition
		try {
			ndcContents = (INodeDistanceCalculator) Class.forName(args[1]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			System.err.println("Err: Class " + args[1] + " was not found.");
			printUsage();
		}
		
		// ClosestNode Searcher Definition
		try {
			maxCategoryUtilitySearcher = (IMaxCategoryUtilitySearcher) Class.forName(args[2]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			System.err.println("Err: Class " + args[2] + " was not found.");
			printUsage();
		}
		
		// Node Updater Definition
		try {
			nodeUpdater = (INodeUpdater) Class.forName(args[3]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			System.err.println("Err: Class " + args[3] + " was not found.");
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
		
		// Build Operator Descriptor for Rapidminer
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
			e.printStackTrace();
		}
		
		// create a data set
		//RandomDataset ds = new RandomDataset(15, 10, 60);
		//ds.printRandomMatrix();

		GrouplensDataset ds = new GrouplensDataset(null);
		TreeBuilder<Integer> treeBuilder = new TreeBuilder<Integer>(

//		FloareaDataset ds = new FloareaDataset(null);
		
		// create TreeBuilder
		//TreeBuilder<Double> treeBuilder = new TreeBuilder<Double>(
				rapidminerOperatorDescription,
				ds,
				ndcUsers,
				ndcContents,
				maxCategoryUtilitySearcher,
				nodeUpdater
				);
		treeBuilder.cluster();
	}

	private static void printUsage() {
		System.out.println("Usage: <fully-qualified-class-name-of-NodeDistanceCalcuator-for-users> " +
				"<fully-qualified-name-of-NodeDistanceCalcuator-for-contents>" + 
				"<fully-qualified-name-of-ClosestNodesSearcher>" + 
				"<fully-qualified-name-of-NodeUpdater>\n" +
				"Example: modules.SimpleNodeDistanceCalculator " +
				"modules.SimpleNodeDistanceCalculator " +
				"modules.SimpleClosestNodesSearcher " +
				"modules.SimpleNodeUpdater");
		System.exit(-1);

	}


	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
