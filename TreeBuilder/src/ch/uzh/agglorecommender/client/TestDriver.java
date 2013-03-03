package ch.uzh.agglorecommender.client;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.client.IDataset.DataSetSplit;
import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;
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
					getTrainingDataset(),
					TreeComponentFactory.getInstance());
			log.info("Starting new run ...");
			clusterResult = tb.startClustering(
					cla.serializeRun,
					in,
					cla.nodeUpdater,
					cla.useUserMetaDataForClustering,
					cla.useContentMetaDataForClustering);
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
		Evaluator eb = new Evaluator();
		RecommendationBuilder rb = new RecommendationBuilder(trainingOutput,0,0);
		
		// Run Recommendation Type 1
		System.out.println("-------------------------------");
		System.out.println("Starting Recommendation Type 1");
		System.out.println("-------------------------------");
		
		InitialNodesCreator testSet = new InitialNodesCreator(
				getTestDataset(),
				TreeComponentFactory.getInstance());
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
		
		return new TreeBuilder();		
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
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
