package ch.uzh.agglorecommender.client;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.ClusteringController;
import ch.uzh.agglorecommender.clusterer.GUIClusteringController;
import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.SimpleClusteringController;
import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.ClusterInteraction;
import ch.uzh.agglorecommender.recommender.Searcher;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;

import com.beust.jcommander.JCommander;

public class TestDriver {
	
	/**
	 * The command line arguments, handled by JCommander.
	 */
	private static CommandLineArgs cla = new CommandLineArgs();
	
	private static Logger log = TBLogger.getLogger(TestDriver.class.toString());
	
	public static void main(String[] args) throws Exception {
		
		// Process Command Line Arguments
		JCommander jc = new JCommander(cla, args);
		jc.setProgramName("AggloRecommender");
		
		if (cla.help) {
			jc.usage();
			return;
		}
		
		log.info("Passed CommandLineArgs: " + Arrays.asList(args).toString());
		
		if (cla.datasetProperties.get(0).equalsIgnoreCase(CommandLineArgs.randomDatasetIdentifier)) {
			// we perform a test clustering run with a randomly generated data set		
			if (cla.datasetProperties.size() == 1) {
				training(new RandomDataset());
			} else if (cla.datasetProperties.size() == 4) {
				training(new RandomDataset(
						Integer.parseInt(cla.datasetProperties.get(1)),
						Integer.parseInt(cla.datasetProperties.get(2)),
						Double.parseDouble(cla.datasetProperties.get(3))));
			} else {
				System.err.println("Not possible to instatiate random data set.\n" +
						"Please specifie the random data set with zero or three arguments.\n" +
						"E.g.: \"-p random\" or \"-p random 100 50 12.5\". \nApplication terminates.");
			}
			
		} else {
			
			// we read the data for clustering from the locations
			// specified in the data set properties xml-file passed at startup
			InputParser parser = new InputParser(new File(cla.datasetProperties.get(0)));
			
			// perform the clustering
			ClusterResult clusterResult = training(parser.getTrainigsDataset());
			
			// instantiate recommendation
			Searcher searcher 		= new Searcher(clusterResult, parser.getTestDataset(), new File(cla.datasetProperties.get(0)));
			ClusterInteraction rp	= new ClusterInteraction(searcher);
			
			// test the quality of the clustering
			test(parser.getTestDataset(), rp);
			
			// start recommendation client
			startClient(rp);
		}
	}
	
	/**
	 * Perform clustering of the passed IDataset.
	 * @param trainingsDataset the data set to cluster
	 * @return a tree of clusters
	 */
	private static ClusterResult training(IDataset trainingsDataset) {
		ClusterResult clusterResult = null;
		if (cla.resumePrevRun != null) {
			// read a serialized TreeBuilder from disc
			log.info("Start loading serialized run at: " + cla.resumePrevRun);
			TreeBuilder tb = (TreeBuilder) ToFileSerializer.deserialize(cla.resumePrevRun);
			// add controller to TreeBuilder
			ClusteringController controller = getClusteringController(tb);
			log.info("Resume clustering ...");
			// continue clustering
			clusterResult = controller.resumeClusteringRun(cla.serializeRun);
		} else {
			// create leaf clusters of a new cluster tree
			InitialNodesCreator in = new InitialNodesCreator(
					trainingsDataset,
					TreeComponentFactory.getInstance());
			// get a new ClusteringController that controls a new TreeBuilder
			ClusteringController controller = getClusteringController(null);
			log.info("Starting new run ...");
			// start clustering with leaves / at bottom of tree
			clusterResult = controller.startNewClusteringRun(cla.nodeUpdater, in, cla.serializeRun);
		}
		return clusterResult;
	}
	
	/**
	 * Attach the specified clustering controller to the passed TreeBuilder
	 * @param treeBuilder the TreeBuilder to control
	 * @return a ClusteringControler that controls the passed TreeBuilder
	 */
	private static ClusteringController getClusteringController(TreeBuilder treeBuilder) {
		if (cla.noGui) {
			return new SimpleClusteringController(treeBuilder);
		} else {
			return new GUIClusteringController(treeBuilder);
		}
	}
	
	/**
	 * Allows the Evaluation of the quality of recommendations given by the system
	 * Recommendation Type 1 delivers quantitative information (RSME/AME)
	 * Recommendation Type 2 delivers qualitative information (the recommendation)
	 * 
	 * @param testDataset the data set to use for testing
	 * @param rp the binding to the cluster hiearchie
	 * @throws Exception 
	 */
	private static void test(IDataset testDataset, ClusterInteraction rp) throws Exception {
		
		// Run Quantitative Evaluation
		System.out.println("-------------------------------");
		System.out.println("Evaluating Clustering");
		System.out.println("-------------------------------");
		InitialNodesCreator testSet = new InitialNodesCreator(testDataset,TreeComponentFactory.getInstance());
		rp.kFoldEvaluation(testSet);
		
	}
	
	/**
	 * Starts the defined client
	 * The DefaultClient is started if no client definition was
	 * given in the command line
	 * 
	 * @param clusterInteraction proxy to interact with the cluster resutl
	 * @throws Exception
	 */
	private static void startClient(ClusterInteraction clusterInteraction) throws Exception{
		
		// Start Client
		System.out.println("-------------------------------");
		System.out.println("Starting Recommendation Client");
		System.out.println("-------------------------------");
		
		cla.client.setController(clusterInteraction);
		cla.client.startService();
		
	}
		
	/**
	 * Must not be instantiated.
	 */
	private TestDriver() { }

}
