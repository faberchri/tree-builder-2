package ch.uzh.agglorecommender.clusterer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.client.IDatasetItem;
import ch.uzh.agglorecommender.client.INormalizer;
import ch.uzh.agglorecommender.client.SerializableRMOperatorDescription;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
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
	
	/**
	 * The data set to cluster.
	 */
	private transient IDataset<?> dataset; 
	
	/**
	 * The set of all root nodes of type user.
	 */
	private Set<INode> userNodes = new  IndexAwareSet<INode>(); 
	
	/**
	 * The set of all root nodes of type content.
	 */
	private Set<INode> contentNodes = new IndexAwareSet<INode>();
	
	/**
	 * The final set of root nodes of type user and type content.
	 */
	private ArrayList<INode> rootNodes = new ArrayList<INode>();
	
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
			IDataset<?> dataset,
			IMaxCategoryUtilitySearcher searcherContent,
			IMaxCategoryUtilitySearcher searcherUsers,
			TreeComponentFactory contentTreeComponentFactory,
			TreeComponentFactory userTreeComponentFactory,
			INodeUpdater nodeUpdater) {
		
		super(SerializableRMOperatorDescription.getOperatorDescription());

		this.dataset = dataset;
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
	 */
	public void resumeClustering(String pathToWriteSerializedObject) {
		log = TBLogger.getLogger(getClass().getName());
		treeVisualizer = new TreeVisualizer();
		cluster(pathToWriteSerializedObject);
	}
	
	/**
	 * Starts a new clustering from scratch.
	 * @param pathToWriteSerializedObject location for the serialization file.
	 * If null no file is created. 
	 */
	public void startClustering(String pathToWriteSerializedObject) {
		initLeafNodes(dataset);	
		cluster(pathToWriteSerializedObject);
	}
	
	/**
	 * Performs the cluster tree creation of the data set.
	 * 
	 * @param pathToWriteSerializedObject location for the serialization file.
	 * If null no file is created. 
	 */
	private void cluster(String pathToWriteSerializedObject) {
				
		// Initialize Visualizer
		treeVisualizer.initVisualization(userNodes, contentNodes);
		
		// Initialize Monitor
		monitor.initMonitoring(userNodes.size(), contentNodes.size());

		// Process Nodes
		while (userNodes.size() >= 2 || contentNodes.size() >= 2) {

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
		
		// Save final root nodes
		rootNodes.addAll(userNodes);
		rootNodes.addAll(contentNodes);
		
		log.info("Clustering completed! Serializing TreeBuilder...");
		// serialize this TreeBuilder if clustering is completed.
		ToFileSerializer.serialize(this, pathToWriteSerializedObject, builderId);
		
		// Create/Update Visualization
		treeVisualizer.visualize();
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
	 * Initializes the leaf nodes of the tree at the start of the clustering process.
	 * For each user and each content item one node is created.
	 * 
	 * @param dataset the data set to cluster.
	 */
	private void initLeafNodes(IDataset<?> dataset) {

		Map<Integer, List<IDatasetItem<?>>> usersMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		Map<Integer, List<IDatasetItem<?>>> contentsMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		
		// sort data items according to user id and content id
		Iterator<?> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<?> datasetItem = (IDatasetItem<?>) it.next();
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
		}
		
		// create for each user and content id one node
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer i : usersMap.keySet()) {
			usersNodeMap.put(i, userTreeComponentFactory.createLeafNode(ENodeType.User, i));
		}		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, contentTreeComponentFactory.createLeafNode(ENodeType.Content, i));
		}
		
		// attach to each node its attributes map
		for (Map.Entry<Integer, List<IDatasetItem<?>>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<?> di : entry.getValue()) {
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( di.getValue());
				attributes.put(contentsNodeMap.get(di.getContentId()), contentTreeComponentFactory.createAttribute(normalizedRating));
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<IDatasetItem<?>>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<?> di : entry.getValue()) {			
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( di.getValue());
				attributes.put(usersNodeMap.get(di.getUserId()), userTreeComponentFactory.createAttribute(normalizedRating));
			}
			contentsNodeMap.get(entry.getKey()).setAttributes(attributes);
			contentNodes.add(contentsNodeMap.get(entry.getKey()));
		}
						
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
		Logger log = TBLogger.getLogger(getClass().getName());
		
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
	 * Returns root nodes for further operations on the trees.
	 * More like a quick solution for the recommender implementation. Could be done more elegant.
	 * The Clusterer Method could return the root nodes.
	 * 
	 * @return the root nodes of the user and the content trees
	 */
	public ArrayList<INode> getRootNodes(){
		return rootNodes;
	}
}
