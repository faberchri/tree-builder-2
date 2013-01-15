package ch.uzh.agglorecommender.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treesearch.IMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;

import com.beust.jcommander.JCommander;

public class TestDriver {
	
	private static IDataset dataset = null;
	private static IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherContents = null;
	private static IMaxCategoryUtilitySearcher maxCategoryUtilitySearcherUsers = null;
	private static INodeUpdater nodeUpdater = null;

	private static CommandLineArgs cla = new CommandLineArgs();
	private static JCommander jc;

	public static void main(String[] args) {

		Logger log = TBLogger.getLogger(TestDriver.class.toString());
			
		jc = new JCommander(cla, args);
		jc.setProgramName("TreeBuilder");
		
		log.info("Passed CommandLineArgs: " + Arrays.asList(args).toString());
		
		checkPathForReadAccess(cla.datasetFile);
		checkPathForReadAccess(cla.resumePrevRun);
		checkPathForWriteAccess(cla.serializeRun);
		
		TreeBuilder tb = null;
		if (cla.resumePrevRun != null) {
			log.info("Start loading serailized run at: " + cla.resumePrevRun);
			tb = (TreeBuilder) ToFileSerializer.deserialize(cla.resumePrevRun);
			log.info("Resume clustering ...");
			tb.resumeClustering(cla.serializeRun);
		} else {
			log.info("Starting new run ...");
			tb = createNewTreeBuilder();
			tb.startClustering(cla.serializeRun);
		}	
	}
	
	/**
	 * Checks read access to passed path.
	 * Application exits if no read access is granted.
	 * @param path to check access.
	 */
	private static void checkPathForReadAccess(String path) {
		if (path == null) return;
		try {
			FileInputStream fis = new FileInputStream(path);
			fis.close();
		} catch (FileNotFoundException e) {
			TBLogger.getLogger(TestDriver.class.getName()).severe(
					"The specified path " + path + " is not a valid read location due " +
					"to one of the following reasons: The file does not exist, "+
					"is a directory rather than a regular file, "+
					"or for some other reason cannot be opened for reading.");
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks write access to passed path.
	 * Application exits if no write access is granted.
	 * @param path to check access.
	 */
	private static void checkPathForWriteAccess(String path) {
		if (path == null) return;
		File file = new File(path);
		if (! file.exists()) {
			try {
				new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				TBLogger.getLogger(TestDriver.class.getName()).severe(
						"The specified path " + path + " is not a valid write location due " +
								"to one of the following reasons: The file exists but is a directory "+
								"rather than a regular file or does not exist but cannot " +
						"be created, or cannot be opened for any other reason.");
				jc.usage();
				System.exit(-1);
			}
			file.delete();
		} else {
			if (! file.canWrite()) {
				TBLogger.getLogger(TestDriver.class.getName()).severe(
						"The specified path " + path + " is not a valid write location due " +
								"to one of the following reasons: The file exists but is a directory "+
								"rather than a regular file or does not exist but cannot " +
						"be created, or cannot be opened for any other reason.");
				jc.usage();
				System.exit(-1);
			}
		}
	}


	/**
	 * Instantiates the modules as specified in 
	 * the input args and creates a new TreeBuilder.
	 * 
	 * @return a new TreeBuilder instance.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static TreeBuilder createNewTreeBuilder() {

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
		
		// initialize the RapidMiner operator description
		SerializableRMOperatorDescription.setOperatorDescription("groupKey", "key", "iconName");
		
		return new TreeBuilder(
				dataset,
				maxCategoryUtilitySearcherContents,
				maxCategoryUtilitySearcherUsers,
				nodeUpdater
				);		
	}


	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
