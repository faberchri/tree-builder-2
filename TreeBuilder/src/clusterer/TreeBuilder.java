package clusterer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import modules.ClassitAttributeFactory;
import modules.ConcreteNodeFactory;
import modules.IndexAwareSet;
import storing.DBHandling;
import utils.TBLogger;
import utils.ToFileSerializer;
import visualization.TreeVisualizer;
import client.IDataset;
import client.IDatasetItem;
import client.SerializableRMOperatorDescription;

/**
 * 
 * Implementation of COBWEB inspired hierarchical
 * agglomerative two-dimensional clustering algorithm 
 * for media recommendation generation.
 *
 * @param <T> the data type of the media ratings.
 */
public final class TreeBuilder<T extends Number> extends DummyRMOperator implements Serializable {
	
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
	private transient IDataset<T> dataset; 
	
	/**
	 * The set of all root nodes of type user.
	 */
	private Set<INode> userNodes = new  IndexAwareSet<INode>(); 
	
	/**
	 * The set of all root nodes of type content.
	 */
	private Set<INode> contentNodes = new IndexAwareSet<INode>();
	
	/**
	 * The node factory of the clusterer.
	 */
	private NodeFactory nodeFactory;
	
	/**
	 * The factory that creates the attribute object(s) of content nodes.
	 */
	private AttributeFactory contentNodeAttributeFactory;
	
	/**
	 * The factory that creates the attribute object(s) user nodes.
	 */
	private AttributeFactory userNodeAttributeFactory;
	
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
	private transient DBHandling dbHandling;

	/**
	 * Manages the graphical representation of the tree structure visualization.
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
	 * Keeping this reference ensures that the counter gets serialized 
	 * together with the TreeBuilder.
	 */
	private final Counter counter = Counter.getInstance();
	
	
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
			IDataset<T> dataset,
			IMaxCategoryUtilitySearcher searcherContent,
			IMaxCategoryUtilitySearcher searcherUsers,
			INodeUpdater nodeUpdater) {
		
		super(SerializableRMOperatorDescription.getOperatorDescription());

		this.dataset = dataset;
		this.nodeFactory = ConcreteNodeFactory.getInstance();

		this.nodeUpdater = nodeUpdater;
		this.userMCUSearcher = searcherUsers;
		this.contentMCUSearcher = searcherContent;
		this.userNodeAttributeFactory = ClassitAttributeFactory.getInstance();
		this.contentNodeAttributeFactory = ClassitAttributeFactory.getInstance();;
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
				
		// Instantiate DB
		//this.dbHandling = new DBHandling();
		//dbHandling.connect();
				
		// init counter and visualizer
		Counter counter = Counter.getInstance();
		counter.initCounter(userNodes, contentNodes);

		treeVisualizer.initVisualization(counter, userNodes, contentNodes);

        
		// Process Nodes
		while (userNodes.size() >= 2 || contentNodes.size() >= 2) {

			log.info("Get closest user nodes & merge them");
			INode newUserNode = searchAndMergeNode(userNodes, userMCUSearcher);
			
			log.info("Get closest content nodes & merge them");
			INode newMovieNode = searchAndMergeNode(contentNodes, contentMCUSearcher);
			
			// Update Trees with info from other tree on current level - only if nodes merged
			if(newUserNode != null && contentNodes.size() > 1) {
				nodeUpdater.updateNodes(newUserNode,contentNodes); 
			}
			if(newMovieNode != null && userNodes.size() > 1) {
				nodeUpdater.updateNodes(newMovieNode,userNodes);
			}
			
			// Create/Update Visualization
			treeVisualizer.visualize();
			
			// Update counter
			counter.setOpenMovieNode(contentNodes.size());
			counter.setOpenUserNodeCount(userNodes.size());
			counter.addCycle();
			
			// serialize this TreeBuilder if necessary according to specified interval.
			// This writes current TreeBuilder state to disk and
			// allows to terminate clustering process and to resume later.
			// The frequency of serialization can be set with
			// ToFileSerializer.serializationTimeInterval
			ToFileSerializer.serializeConditionally(this, pathToWriteSerializedObject, builderId);
		} 
		log.info("Clustering completed! Serializing TreeBuilder...");
		// serialize this TreeBuilder if clustering is completed.
		ToFileSerializer.serialize(this, pathToWriteSerializedObject, builderId);
		
		// Create/Update Visualization
		treeVisualizer.visualize();
		// Close Database
		//dbHandling.shutdown();
	}
	
	/**
	 * Gets the best merge from the passed set and returns the resulting new node.
	 * 
	 * @param nodes the set of nodes in which the best merge is searched.
	 * @return the merge result or null if no possible merge was found.
	 */
	private INode searchAndMergeNode(Set<INode> nodes, IMaxCategoryUtilitySearcher mcus) {
		INode newNode = null;
		if(nodes.size() >= 2) {
			IMergeResult cN = mcus.getMaxCategoryUtilityMerge(nodes);
			
			log.info("Best node merge has category utility of "
						+cN.getCategoryUtility() +" and includes: " + cN.getNodes());
			
			Counter counter = Counter.getInstance();
			newNode = mergeNodes(cN.getNodes(), nodes, counter);
			
			log.info("cycle "+ counter.getCycleCount() + "| number of open nodes: " + 
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
	private void initLeafNodes(IDataset<T> dataset) {

		Map<Integer, List<IDatasetItem<T>>> usersMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		Map<Integer, List<IDatasetItem<T>>> contentsMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		
		// sort data items according to user id and content id
		Iterator<IDatasetItem<T>> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<T> datasetItem = it.next();
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<T>> li = new ArrayList<IDatasetItem<T>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem<T>> li = new ArrayList<IDatasetItem<T>>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
		}
		
		// create for each user and content id one node
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer i : usersMap.keySet()) {
			usersNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.User));
		}		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.Content));
		}
		
		// attach to each node its attributes map
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
				double normalizedRating = dataset.getNormalizer().normalizeRating( di.getValue());
				attributes.put(contentsNodeMap.get(di.getContentId()), contentNodeAttributeFactory.createAttribute(normalizedRating));
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
				double normalizedRating = dataset.getNormalizer().normalizeRating( di.getValue());
				attributes.put(usersNodeMap.get(di.getUserId()), userNodeAttributeFactory.createAttribute(normalizedRating));
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
	private INode mergeNodes(List<INode> nodesToMerge, Set<INode> openSet, Counter counter) {
		Logger log = TBLogger.getLogger(getClass().getName());
		
		if (nodesToMerge.size() > 1) {
			
			// Create a new node (product of nodesToMerge)
			INode newNode;
			switch (nodesToMerge.get(0).getNodeType()) {
			case User:
				newNode = nodeFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge,					//  we need to pass the contentNodeAttributeFactory since
						contentNodeAttributeFactory); // the attributes of the new user node are content nodes
				counter.addUserNode();
				break;
			case Content:
				newNode = nodeFactory.createInternalNode(
						ENodeType.Content,
						nodesToMerge,				// we need to pass the userNodeAttributeFactory since
						userNodeAttributeFactory);// the attributes of the new content node are user nodes
				counter.addMovieNode();
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
				
				// Create parent/child relationships
// is done on instantiation of node can be removed from here				
//				nodeToMerge.setParent(newNode);
//				newNode.addChild(nodeToMerge);
	
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
	/**For testing only!!
	 * Merges the nodes from nodesToMerge and returns a new parent node.
	 * This method corresponds to mergeNodes(List<INode> nodesToMerge, Set<INode> openSet, Counter counter),
	 * but ignores the counter and is public for easier merging during testing. 
	 */
	
	public INode createTestingMergedNode(List<INode> nodesToMerge, Set<INode> openSet){
		Logger log = TBLogger.getLogger(getClass().getName());
		
		if (nodesToMerge.size() > 1) {
			
			// Create a new node (product of nodesToMerge)
			INode newNode;
			switch (nodesToMerge.get(0).getNodeType()) {
			case User:
				newNode = nodeFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge,					//  we need to pass the contentNodeAttributeFactory since
						contentNodeAttributeFactory); // the attributes of the new user node are content nodes
				break;
			case Content:
				newNode = nodeFactory.createInternalNode(
						ENodeType.Content,
						nodesToMerge,				// we need to pass the userNodeAttributeFactory since
						userNodeAttributeFactory);// the attributes of the new content node are user nodes
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
				
				// Create parent/child relationships
// is done on instantiation of node can be removed from here				
//				nodeToMerge.setParent(newNode);
//				newNode.addChild(nodeToMerge);
	
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
}
