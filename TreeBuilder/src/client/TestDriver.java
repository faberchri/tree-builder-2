package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Logger;

import utils.TBLogger;
import utils.ToFileSerializer;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INodeUpdater;
import clusterer.TreeBuilder;

import com.beust.jcommander.JCommander;

public class TestDriver {

	
	
	private static IDataset dataset = null;
	private static IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherContents = null;
	private static IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherUsers = null;
	private static INodeUpdater nodeUpdater = null;

	/**
	 * @param args args[0]: default data set to load. Fully qualified class name of desired implementation of IDataset.
	 * <br> args[1]: max category utility searcher for content nodes. Fully qualified class name of desired implementation of IMaxCategoryUtilitySearcher.
	 * <br> args[2]: max category utility searcher for user nodes. Fully qualified class name of desired implementation of IMaxCategoryUtilitySearcher.
	 * <br> args[3]: node updater to use. Fully qualified class name of desired implementation of INodeUpdater.
	 * <br> args[4]: node updater to use. Fully qualified class name of desired implementation of INodeUpdater.
	 */
	public static void main(String[] args) {

		Logger log = new TBLogger().getLogger(TestDriver.class.toString());
		
		CommandLineArgs cla = new CommandLineArgs();
		JCommander jc = new JCommander(cla, args);
		jc.setProgramName("TreeBuilder");
		
		log.info("Passed CommandLineArgs: " + Arrays.asList(args).toString());
		
		TreeBuilder tb = null;
		if (cla.resumePrevRun != null) {
			log.info("Start loading serailized run at: " + cla.resumePrevRun);
			tb = loadSerializedTreeBuilder(cla, jc);
			log.info("Resume clustering ...");
			tb.resumeClustering(cla.serializeRun);
		} else {
			log.info("Starting new run ...");
			tb = createNewTreeBuilder(cla, jc);
			tb.startClustering(cla.serializeRun);
		}
		
	}
	
	private static TreeBuilder loadSerializedTreeBuilder(CommandLineArgs cla, JCommander jc) {
		TreeBuilder tb = null;
		try {
			tb = ToFileSerializer.deserialize(cla.resumePrevRun);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return tb;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static TreeBuilder createNewTreeBuilder(CommandLineArgs cla, JCommander jc) {

		// Load specified data set (with default input file)
		try {
			Class klass = Class.forName(cla.nameOfDataset);
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
					dataset = (IDataset) constructor.newInstance(cla.datasetFile);
					break;
				}
			}
			if (constructor == null) {
				throw new NoSuchMethodException();
			}	
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + cla.nameOfDataset + " was not found.");
			jc.usage();
			System.exit(-1);
		} 
				
		// load specified max category utility searcher for contents
		try {
			maxCategoryUtilitySearcherContents = 
					(IMaxCategoryUtilitySearcher) Class.forName(cla.nameOfmaxCategoryUtilitySearcherContents).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class "
					+ cla.nameOfmaxCategoryUtilitySearcherContents + " was not found.");
			jc.usage();
			System.exit(-1);
		}
		
		// load specified max category utility searcher for users
		try {
			maxCategoryUtilitySearcherUsers =
					(IMaxCategoryUtilitySearcher) Class.forName(cla.nameOfmaxCategoryUtilitySearcherUsers).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class "
					+ cla.nameOfmaxCategoryUtilitySearcherUsers + " was not found.");
			jc.usage();
			System.exit(-1);
		}
		
		// load specified node updater
		try {
			nodeUpdater = (INodeUpdater) Class.forName(cla.nameOfNodeUpdater).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe("Class " + cla.nameOfNodeUpdater + " was not found.");
			jc.usage();
			System.exit(-1);
		}

		SerializableRMOperatorDescription.setOperatorDescription("groupKey", "key", "iconName");
		
		return new TreeBuilder(
				dataset,
				maxCategoryUtilitySearcherContents,
				maxCategoryUtilitySearcherUsers,
				nodeUpdater
				);
		
	}

//	private static void printUsage() {
//		System.out.println("Usage:\n" +
//				"<fully-qualified-class-name-of-IDataset> " +
//				"<fully-qualified-name-of-IMaxCategoryUtilitySearcher-for-contents>" + 
//				"<fully-qualified-class-name-of-IMaxCategoryUtilitySearcher-for-users> " +
//				"<fully-qualified-name-of-SimpleNodeUpdater>\n" +
//				"Example: client.GrouplensDataset " +
//				"modules.CobwebMaxCategoryUtilitySearcher " +
//				"modules.CobwebMaxCategoryUtilitySearcher " +
//				"modules.SimpleNodeUpdater");
//		System.exit(-1);
//
//	}
	

	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
