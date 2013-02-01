package ch.uzh.agglorecommender.clusterer;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.InitialNodesCreator;
import ch.uzh.agglorecommender.client.SerializableRMOperatorDescription;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treesearch.IMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.IMergeResult;
import ch.uzh.agglorecommender.clusterer.treesearch.IndexAwareSet;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.util.DBHandler;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;
import ch.uzh.agglorecommender.visu.TreeVisualizer;

/**
 * 
 * Implementation of COBWEB inspired hierarchical
 * agglomerative two-dimensional clustering algorithm 
 * for media recommendation generation.
 *
 */
public final class TreeBuilder extends DummyRMOperator implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
//	/**
//	 * The data set to cluster.
//	 */
//	private transient IDataset<?> dataset; 
	
	/**
	 * The set of all root nodes of type user.
	 */
	private Set<INode> userNodes = new  IndexAwareSet<INode>(); 
	
	/**
	 * The set of all root nodes of type content.
	 */
	private Set<INode> contentNodes = new IndexAwareSet<INode>();
	
//	/**
//	 * The final set of root nodes of type user and type content.
//	 */
//	private ArrayList<INode> rootNodes = new ArrayList<INode>();
	
	/**
	 * The result of the clustering process.
	 */
	private ClusterResult result;
			
	/**
	 * The tree component factory of the content tree.
	 */
	private TreeComponentFactory contentTreeComponentFactory;
	
	/**
	 * The tree component factory of the user tree.
	 */
	private TreeComponentFactory userTreeComponentFactory;
	
	/**
	 * The updater which performs the introduction 
	 * of a node as attribute in the nodes of the other type.
	 */
	private INodeUpdater nodeUpdater;
		
	/**
	 * The searcher for the best user nodes merge.
	 */
	private IMaxCategoryUtilitySearcher userMCUSearcher;
	
	/**
	 * The searcher for the best content nodes merge.
	 */
	private IMaxCategoryUtilitySearcher contentMCUSearcher;
	
	/**
	 * Handles storing of nodes to db
	 */
	private transient DBHandler dbHandling;

	/**
	 * Manages the graphical representation of the tree structure ch.uzh.agglorecommender.visu.
	 */
	private transient TreeVisualizer treeVisualizer;
	
	/**
	 * The logger of this class.
	 */
	private transient Logger log = TBLogger.getLogger(getClass().getName());
	
	/**
	 * Each TreeBuilder gets a unique id, which is included in the filename of the
	 * serialized object.
	 */
	private final UUID builderId = UUID.randomUUID();
	
	/**
	 * Keeping this reference ensures that the monitor gets serialized 
	 * together with the TreeBuilder.
	 */
	private final Monitor monitor = Monitor.getInstance();
	
	/**
	 * Instantiates a new tree builder which can create a cluster tree based on the passed data set.
	 * 
	 * @param rapidminerOperatorDescription Data container for name, class, short name,
	 * path and the description of an operator. 
	 * @param dataset the data set to cluster.
	 * @param searcherUsers the best merge searcher for nodes of type user.
	 * @param searcherContent the best merge searcher for nodes of type content.
	 * @param nodeUpdater the node updater used in the clustering process.
	 */
	public TreeBuilder(
			IMaxCategoryUtilitySearcher searcherContent,
			IMaxCategoryUtilitySearcher searcherUsers,
			TreeComponentFactory contentTreeComponentFactory,
			TreeComponentFactory userTreeComponentFactory,
			INodeUpdater nodeUpdater) {
		
		super(SerializableRMOperatorDescription.getOperatorDescription());

		this.nodeUpdater = nodeUpdater;
		this.userMCUSearcher = searcherUsers;
		this.contentMCUSearcher = searcherContent;
		this.contentTreeComponentFactory = contentTreeComponentFactory;
		this.userTreeComponentFactory = userTreeComponentFactory;
		this.treeVisualizer = new TreeVisualizer();	
	}
		
	/**
	 * Resumes a previously start clustering process.
	 * 
	 * @param pathToWriteSerializedObject location for the new serialization file.
	 * If null no file is created.
	 * @return the result of the clustering process
	 */
	public ClusterResult resumeClustering(String pathToWriteSerializedObject) {
		log = TBLogger.getLogger(getClass().getName());
		treeVisualizer = new TreeVisualizer();
		return cluster(pathToWriteSerializedObject);
	}
	
	/**
	 * Starts a new clustering process from scratch.
	 * 
	 * @param pathToWriteSerializedObject location for the serialization file.
	 * If null no file is created.
	 * @return the result of the clustering process
	 */
	public ClusterResult startClustering(String pathToWriteSerializedObject, InitialNodesCreator leafNodes) {
		log = TBLogger.getLogger(getClass().getName());
		userNodes.clear();
		contentNodes.clear();
		this.result = new ClusterResult(
				leafNodes.getUserLeaves(), leafNodes.getContentLeaves(),
				null, null, builderId);
		initNodeSets(leafNodes);
		return cluster(pathToWriteSerializedObject);
	}
	
	/**
	 * Creates references to the leaf nodes in the set of nodes to cluster.
	 * 
	 * @param leafNodes the initial leaf nodes 
	 */
	private void initNodeSets(InitialNodesCreator leafNodes) {
		for (INode n : leafNodes.getContentLeaves().values()) {
			contentNodes.add(n);
		}	
		for (INode n : leafNodes.getUserLeaves().values()) {
			userNodes.add(n);
		}
	}
	
	/**
	 * Performs the cluster tree creation of the data set.
	 * 
	 * @param pathToWriteSerializedObject location for the serialization file.
	 * If null no file is created.
	 * 
	 * @return The resulting @code{cluster result} object.
	 */
	private ClusterResult cluster(String pathToWriteSerializedObject) {
				
		// Initialize Visualizer
		treeVisualizer.initVisualization(userNodes, contentNodes);
		
		// Initialize Monitor
		monitor.initMonitoring(userNodes.size(), contentNodes.size());

		// Process Nodes
		while (userNodes.size() >= 2 || contentNodes.size() >= 2) {

			// check if clustering is interrupted
			interrupt();
			
			log.info("Get closest user nodes & merge them");
			INode newUserNode = searchAndMergeNode(userNodes, userMCUSearcher);
			
			log.info("Get closest content nodes & merge them");
			INode newContentNode = searchAndMergeNode(contentNodes, contentMCUSearcher);
			
			// Update Trees with info from other tree on current level - only if nodes merged
			if(newUserNode != null && contentNodes.size() > 1) {
				nodeUpdater.updateNodes(newUserNode,contentNodes); 
			}
			if(newContentNode != null && userNodes.size() > 1) {
				nodeUpdater.updateNodes(newContentNode,userNodes);
			}
			
			// Create/Update Visualization
			treeVisualizer.visualize();
			
			// Update Monitor
			monitor.update(userNodes.size(),contentNodes.size());
			
			// serialize this TreeBuilder if necessary according to specified interval.
			// This writes current TreeBuilder state to disk and
			// allows to terminate clustering process and to resume later.
			// The frequency of serialization can be set with
			// ToFileSerializer.serializationTimeInterval
			ToFileSerializer.serializeConditionally(this, pathToWriteSerializedObject, builderId);
		} 
				
		log.info("Clustering terminated! Serializing TreeBuilder...");
		// serialize this TreeBuilder if clustering is completed.
		ToFileSerializer.serialize(this, pathToWriteSerializedObject, builderId);
		
		// Create/Update Visualization
		treeVisualizer.visualize();
		
		if (contentNodes.size() == 1 && userNodes.size() == 1) {
			result = new ClusterResult(
					result.getUserTreeLeavesMap(), result.getContentTreeLeavesMap(), 
					userNodes.iterator().next(), contentNodes.iterator().next(), builderId);
		} else {
			log.severe("clustering terminated before the user or content cluster forests converged to trees");
			System.exit(-1);
		}
		return result;
		
	}
		
	/**
	 * Gets the best merge from the passed set and returns the resulting new node.
	 * 
	 * @param nodes the set of nodes in which the best merge is searched.
	 * @return the merge result or null if no possible merge was found.
	 */
	private INode searchAndMergeNode(Set<INode> nodes, IMaxCategoryUtilitySearcher mcus) {
		INode newNode = null;
		if(nodes.size() > 1) {
			IMergeResult cN = mcus.getMaxCategoryUtilityMerge(nodes);
			
			log.info("Best node merge has category utility of "
						+cN.getCategoryUtility() +" and includes: " + cN.getNodes());
			
			Monitor counter = Monitor.getInstance();
			newNode = mergeNodes(cN.getNodes(), nodes);
			log.info("cycle "+ monitor.getCycleCount() + "| number of open nodes: " + 
						nodes.size() + "\t elapsed time [s]: "+ counter.getElapsedTime());
//			treeVisualizer.printAllOpenUserNodes();
		}
		return newNode;
	}
	
	
	/**
	 * Creates a new node and initializes the nodes attributes based on a list of close nodes.
	 * The list of close nodes become the children of the new node.
	 * 
	 * @param nodesToMerge close nodes which are
	 * used to initialize a new node and will form the new nodes children. 
	 * These nodes are removed from the open set.
	 * 
	 * @param openSet the set of nodes to which the new node is added
	 * and from which the merged nodes are removed. 
	 * 
	 * @return a new node which has the {@code nodesToMerge} as children. 
	 */
	private INode mergeNodes(List<INode> nodesToMerge, Set<INode> openSet) {
		
		if (nodesToMerge.size() > 1) {
			
			// Create a new node (product of nodesToMerge)
			INode newNode;
			switch (nodesToMerge.get(0).getNodeType()) {
			case User:
				newNode = userTreeComponentFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge); 
				break;
			case Content:
				newNode = contentTreeComponentFactory.createInternalNode(
						ENodeType.Content,
						nodesToMerge);
				break;
			default:
				newNode = null;
				log.severe("Err: Not supported node encountered in: " + getClass().getSimpleName());
				System.exit(-1);
				break;
			}
			
			// Add new node to openset
			openSet.add(newNode);
			log.fine("New node added to open set: " + newNode);
			
			// Updating relationships and remove
			for (INode nodeToMerge : nodesToMerge) {	
				
				// Remove merged Nodes
				if (!openSet.remove(nodeToMerge)) {
					log.severe("Err: Removal of merged node (" + nodeToMerge + ") from " +openSet +" failed, in: " + getClass().getSimpleName());
					System.exit(-1);
				}
			}
		
			return newNode;
			
		} 
		else {
			log.severe("Err: Merge attempt with 1 or less nodes, in: " + getClass().getSimpleName());
			System.exit(-1);
		}
		return null;
	}
	
	/**
	 * Checks TreeVisualizer if clustering process is interrupted. If true 
	 * the TreeVisualizer is polled every 0.5 s to check for status change.
	 */
	private void interrupt() {
		while (treeVisualizer.isPaused()) {
			try {
				synchronized(this) {
					wait(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
