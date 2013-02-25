package ch.uzh.agglorecommender.client;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.client.IDataset.DataSetSplit;
import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.SharedTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.IMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.SharedMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.evaluator.EvaluationBuilder;
import ch.uzh.agglorecommender.recommender.treeutils.NodeInserter;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;

import com.beust.jcommander.JCommander;

public class TestDriver {
	
	protected static CommandLineArgs cla = new CommandLineArgs();
	private static JCommander jc;
	
	// Create Logger
	private static Logger log = TBLogger.getLogger(TestDriver.class.toString());
	

	public static void main(String[] args) {
		

		// Process Command Line Arguments
		jc = new JCommander(cla, args);
		jc.setProgramName("TreeBuilder");
		
		log.info("Passed CommandLineArgs: " + Arrays.asList(args).toString());
		
		test(training());
//		insert(training(), cla.userTreeComponentFactory, new Node(ENodeType.User, 0));
	}
	
	private static ClusterResult training() {
		// Build Tree
		TreeBuilder tb = null;
		ClusterResult clusterResult = null;
		if (cla.resumePrevRun != null) {
			log.info("Start loading serailized run at: " + cla.resumePrevRun);
			tb = (TreeBuilder) ToFileSerializer.deserialize(cla.resumePrevRun);
			log.info("Resume clustering ...");
			clusterResult = tb.resumeClustering(cla.serializeRun);
		} else {
			tb = createNewTreeBuilder();
			InitialNodesCreator in = new InitialNodesCreator(
					getTrainingDataset(),getContentMetaDataset(),getUserMetaDataset(),
					cla.contentTreeComponentFactory,
					cla.userTreeComponentFactory);
			log.info("Starting new run ...");
			clusterResult = tb.startClustering(cla.serializeRun, in);
		}
		return clusterResult;
	}	
	
	/**
	 * Allows the Evaluation of the quality of recommendations given by the system
	 * Recommendation Type 1 delivers quantitative information (RSME/AME)
	 * Recommendation Type 2 delivers qualitative information (the recommendation)
	 * 
	 * @param trainingOutput the trainingCluster for evaluation
	 */
	private static void test(ClusterResult trainingOutput) {
				
		// Instantiate Evaluations Builder
		EvaluationBuilder eb = new EvaluationBuilder();
		RecommendationBuilder rb = new RecommendationBuilder(trainingOutput,0,0);
		
		// Run Recommendation Type 1
		System.out.println("-------------------------------");
		System.out.println("Starting Recommendation Type 1");
		System.out.println("-------------------------------");
		
		InitialNodesCreator testSet = new InitialNodesCreator(
				getTestDataset(),getContentMetaDataset(),getUserMetaDataset(),
				cla.contentTreeComponentFactory,
				cla.userTreeComponentFactory);
		Map<INode,Integer> testNodes = eb.getTestUsers(testSet);
		Map<String, Double> eval = eb.kFoldEvaluation(testNodes, rb);
		
		if(eval != null){
			System.out.println("=> Calculated Evaluation Values: " + eval.toString());
		}
		
		// Recommendation Type 2
		System.out.println("-------------------------------");
		System.out.println("Starting Recommendation Type 2");
		System.out.println("-------------------------------");
		
		Map<INode, IAttribute> testRatings = eb.rateRandomContent(trainingOutput,3); // Ratings
		Map<String,String> testDemographics = eb.defineDemographics(); // Demographics
		INode testUser = eb.createTestUser(testRatings,testDemographics); // Create User with Ratings & Demographics
		Map<INode,IAttribute> unsortedRecommendation = rb.runRecommendation(testUser); // Create Recommendation
		ArrayList<IAttribute> sortedRecommendation = rb.rankRecommendation(unsortedRecommendation,1, 100); // Pick Top Movies for User
		
		if(sortedRecommendation != null){
			System.out.println("=> Recommended Movies: ");
			for(IAttribute recommendation: sortedRecommendation){
				if(recommendation.getMeta() != null){
					System.out.println(recommendation.getMeta().get(1) + " -> Rating: " + recommendation.getMeanOfRatings());
				}
			}
		}
	}
	
	/**
	 * Allows insertion of a new node to an existing tree
	 * 
	 * @param tree the tree to insert the node
	 * @param treeComponentFactory the tree component factory that should be used
	 * @param inputNode the node that should be inserted
	 */
	private static void insert(ClusterResult tree, TreeComponentFactory treeComponentFactory, INode inputNode) {
		NodeInserter nodeInserter = new NodeInserter(tree,treeComponentFactory);
		nodeInserter.insert(inputNode);
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
	
	private static IDataset<?> getContentMetaDataset() {
//		System.out.println("getting content meta dataset");
		return getDataset(cla.metaFile,DataSetSplit.CONTENTMETA);
	}
	
	private static IDataset<?> getUserMetaDataset() {
//		System.out.println("getting user meta dataset");
		return getDataset(cla.metaFile,DataSetSplit.USERMETA);
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
			Constructor<?>[] constructors = null;
			if(split == DataSetSplit.TEST || split == DataSetSplit.TRAINING){
				constructors = cla.datasetType.getConstructors();
			}
			else {
				constructors = cla.metasetType.getConstructors();
//				System.out.println("Trying metaset opening: " + constructors.toString());
			}
			
			for (Constructor<?> constructor : constructors) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				
				// If Constructor has no parameters
				if (parameterTypes.length == 0) {
					dataset = (IDataset<?>) constructor.newInstance();
					break;
				}
				
				// ?
//				if (parameterTypes.length == 1 && parameterTypes[0] == File.class) {
//					// training and test data set is the same
//					dataset = (IDataset<?>) constructor.newInstance(inputFile);
//					break;
//				}
				
				// If Constructor has dataset and split parameter
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
		if (factory instanceof SharedTreeComponentFactory) {
			return new SharedMaxCategoryUtilitySearcher();
		}
		TBLogger.getLogger(TestDriver.class.getName()).severe("No IMaxCategoryUtilitySearcher" +
				" corresponds to TreeComponentFactory "
				+ factory.toString() + ".");
		jc.usage();
		System.exit(-1);
		return null;
	}

	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
