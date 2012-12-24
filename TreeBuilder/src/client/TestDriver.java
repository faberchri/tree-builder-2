package client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import utils.TBLogger;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INodeUpdater;
import clusterer.TreeBuilder;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.plugin.Plugin;

public class TestDriver {

	private static final String rapidminerOperatorJarFileName = "treebuilder.jar";

	/**
	 * @param args args[0]: default data set to load. Fully qualified class name of desired implementation of IDataset.
	 * <br> args[1]: max category utility searcher for content nodes. Fully qualified class name of desired implementation of IMaxCategoryUtilitySearcher.
	 * <br> args[2]: max category utility searcher for user nodes. Fully qualified class name of desired implementation of IMaxCategoryUtilitySearcher.
	 * <br> args[3]: node updater to use. Fully qualified class name of desired implementation of INodeUpdater.
	 * 
	 */
	public static void main(String[] args) {

		if (args.length != 4) {
			printUsage();
		}
		
		IDataset dataset = null;
		IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherContents = null;
		IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherUsers = null;
		INodeUpdater nodeUpdater = null;
		
		// Load specified data set (with default input file)
		try {
			Class klass = Class.forName(args[0]);
			Constructor[] constructors = klass.getConstructors();
			Constructor constructor = null;
			for (Constructor aConstructor : constructors) {
				Class[] parameterTypes = aConstructor.getParameterTypes();
				if (parameterTypes.length == 0) {
					constructor = aConstructor;
					dataset = (IDataset) constructor.newInstance();
					break;
				}
				if (parameterTypes.length == 1 && parameterTypes[0] == File.class) {
					constructor = aConstructor;
					dataset = (IDataset) constructor.newInstance((Object)null);
					break;
				}
			}
			if (constructor == null) {
				throw new NoSuchMethodException();
			}
			
		
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + args[0] + " was not found.");
			printUsage();
		} 
				
		// load specified max category utility searcher for contents
		try {
			maxCategoryUtilitySearcherContents = (IMaxCategoryUtilitySearcher) Class.forName(args[1]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + args[1] + " was not found.");
			printUsage();
		}
		
		// load specified max category utility searcher for users
		try {
			maxCategoryUtilitySearcherUsers = (IMaxCategoryUtilitySearcher) Class.forName(args[2]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + args[2] + " was not found.");
			printUsage();
		}
		
		// load specified node updater
		try {
			nodeUpdater = (INodeUpdater) Class.forName(args[3]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			printUsage();
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + args[3] + " was not found.");
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
		

		TreeBuilder treeBuilder = new TreeBuilder(
				rapidminerOperatorDescription,
				dataset,
				maxCategoryUtilitySearcherContents,
				maxCategoryUtilitySearcherUsers,
				nodeUpdater
				);
		
		treeBuilder.cluster();
		

	}

	private static void printUsage() {
		System.out.println("Usage:\n" +
				"<fully-qualified-class-name-of-IDataset> " +
				"<fully-qualified-name-of-IMaxCategoryUtilitySearcher-for-contents>" + 
				"<fully-qualified-class-name-of-IMaxCategoryUtilitySearcher-for-users> " +
				"<fully-qualified-name-of-SimpleNodeUpdater>\n" +
				"Example: client.GrouplensDataset " +
				"modules.CobwebMaxCategoryUtilitySearcher " +
				"modules.CobwebMaxCategoryUtilitySearcher " +
				"modules.SimpleNodeUpdater");
		System.exit(-1);

	}


	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
