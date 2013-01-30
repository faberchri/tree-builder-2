package ch.uzh.agglorecommender.client;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.IMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.evaluator.EvaluationBuilder;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;

import com.beust.jcommander.JCommander;

public class TestDriver {
	
	private static CommandLineArgs cla = new CommandLineArgs();
	private static JCommander jc;

	public static void main(String[] args) {
		
		// Create Logger
		Logger log = TBLogger.getLogger(TestDriver.class.toString());
		
		// Process Command Line Arguments
		jc = new JCommander(cla, args);
		jc.setProgramName("TreeBuilder");
		
		log.info("Passed CommandLineArgs: " + Arrays.asList(args).toString());
		
		// Build Tree
		TreeBuilder tb = null;
		ClusterResult clusterResult = null;
		if (cla.resumePrevRun != null) {
			log.info("Start loading serailized run at: " + cla.resumePrevRun);
			tb = (TreeBuilder) ToFileSerializer.deserialize(cla.resumePrevRun);
			log.info("Resume clustering ...");
			clusterResult = tb.resumeClustering(cla.serializeRun);
		} else {
			log.info("Starting new run ...");
			tb = createNewTreeBuilder();
			InitalNodesCreator in = new InitalNodesCreator(
					getTrainingDataset(),
					cla.contentTreeComponentFactory,
					cla.userTreeComponentFactory);
			clusterResult = tb.startClustering(cla.serializeRun, in);
		}
		


		
		// Instantiate Evaluations Builder
		EvaluationBuilder eb = new EvaluationBuilder();
		RecommendationBuilder rb = new RecommendationBuilder(tb,0,0);
		
		// Recommendation Type 1 -> rmse calculation
		// Instantiate Recommendation & Evaluation Builders
		InitalNodesCreator testSet = new InitalNodesCreator(
				getTestDataset(),
				cla.contentTreeComponentFactory,
				cla.userTreeComponentFactory);
		Set<INode> inputNodes = eb.pickTestUser(testSet,1);
		double rmse = eb.kFoldEvaluation(inputNodes, rb);
		System.out.println("Calculated RMSE: " + rmse);
		
		// Recommendation Type 2 -> no rmse calculation possible
		INode inputNode = eb.createRandomUser();
		Map<INode,IAttribute> recommendedMovies = rb.runRecommendation(inputNode);
		System.out.println("Recommended Movies: " + recommendedMovies.keySet().toString());
	}
	
	/**
	 * Instantiates the modules as specified in 
	 * the input args and creates a new TreeBuilder.
	 * 
	 * @return a new TreeBuilder instance.
	 */
	private static TreeBuilder createNewTreeBuilder() {
		
		// initialize the RapidMiner operator description
		SerializableRMOperatorDescription.setOperatorDescription("groupKey", "key", "iconName");
		
		return new TreeBuilder(
				getSearcher(cla.contentTreeComponentFactory),
				getSearcher(cla.userTreeComponentFactory),
				cla.contentTreeComponentFactory,
				cla.userTreeComponentFactory,
				cla.nodeUpdater);		
	}
	
	private static IDataset<?> getTestDataset() {
		return getDataset(cla.testFile, DataSetSplit.TEST);
	}
	
	private static IDataset<?> getTrainingDataset() {
		return getDataset(cla.trainingFile, DataSetSplit.TRAINING);
	}
	
	/**
	 * Instantiates the data set object to process. Data are loaded from the specified
	 * file or the default file.
	 * 
	 * @return the IDataset to process
	 */
	private static IDataset<?> getDataset(File inputFile, DataSetSplit split) {
		// Load specified data set (with default input file)	
		IDataset<?> dataset = null;
		try {
			Constructor<?>[] constructors = cla.datasetType.getConstructors();
			for (Constructor<?> constructor : constructors) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length == 0) {
					dataset = (IDataset<?>) constructor.newInstance();
					break;
				}
//				if (parameterTypes.length == 1 && parameterTypes[0] == File.class) {
//					// training and test data set is the same
//					dataset = (IDataset<?>) constructor.newInstance(inputFile);
//					break;
//				}
				if (parameterTypes.length == 2 && parameterTypes[0] == File.class && parameterTypes[1] == DataSetSplit.class) {
					dataset = (IDataset<?>) constructor.newInstance(inputFile, split);
					break;
				}
			}
			if (dataset == null) {
				throw new NoSuchMethodException();
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			jc.usage();
			System.exit(-1);
		}
		return dataset;
	}
	
	
	/**
	 * Selects the correct {@link IMaxCategoryUtilitySearcher} for the passed {@link TreeComponentFactory}. 
	 * @param factory the {@link TreeComponentFactory} for which a searcher should be obtained.
	 * @return a new {@link IMaxCategoryUtilitySearcher} instance.
	 */
	private static IMaxCategoryUtilitySearcher getSearcher(TreeComponentFactory factory) {
		if ( factory instanceof ClassitTreeComponentFactory) {
			return new ClassitMaxCategoryUtilitySearcher();
		}
		if (factory instanceof CobwebTreeComponentFactory) {
			return new CobwebMaxCategoryUtilitySearcher();
		}
		TBLogger.getLogger(TestDriver.class.getName()).severe("No IMaxCategoryUtilitySearcher" +
				" corresponds to TreeComponentFactory "
				+ factory.toString() + ".");
		jc.usage();
		System.exit(-1);
		return null;
	}

	protected enum DataSetSplit{
		TRAINING, TEST
	}

	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
